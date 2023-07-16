package com.example.myapplication;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BillActivity extends AppCompatActivity {

    private TextView billTextView;
    private Button printButton;

    private DatabaseReference billReference;
    private Button saveButton;
    private Button cancelButton;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();

        // Set up the TextView to display the bill
        billTextView = findViewById(R.id.billTextView);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get the selected food items from the intent
        List<FoodItem> foodItems = getIntent().getParcelableArrayListExtra("foodItems");

        // Generate the bill text
        StringBuilder billText = new StringBuilder();
        billText.append("		                            Restro Bill\n");
        billText.append("	                       23232, JAVA CITY,\n");
        billText.append("	                                NY, USA\n");
        billText.append("	                        TEL: 6363956838\n");
        billText.append("----------------------------------------------------------------------\n");
        billText.append("Date: ").append(getCurrentDate()).append("\n");
        billText.append("----------------------------------------------------------------------\n");

        for (FoodItem foodItem : foodItems) {
            String itemName = foodItem.getName();
            String itemPrice = foodItem.getPrice();
            String itemQuantity = String.valueOf(foodItem.getQuantity());

            billText
                    .append("Item ").append(foodItems.indexOf(foodItem) + 1)
                    .append(" - ").append(itemName)
                    .append(" - ").append(itemPrice)
                    .append(" x ").append(itemQuantity)
                    .append("\n");
        }

        billText.append("----------------------------------------------------------------------\n");

        // Calculate the total amount
        double totalAmount = 0.0;
        for (FoodItem foodItem : foodItems) {
            totalAmount += Double.parseDouble(foodItem.getPrice().substring(1)) * foodItem.getQuantity();
        }

        double foodTaxPercentage = 0.1;
        double localTaxPercentage = 0.05;

        double subtotal = totalAmount;
        double foodTax = subtotal * foodTaxPercentage;
        double localTax = subtotal * localTaxPercentage;
        totalAmount = subtotal + foodTax + localTax;
        // Assuming 5% local tax

        billText.append("Subtotal: \u20B9").append(String.format("%.2f", subtotal)).append("\n");
        billText.append("Food Tax: \u20B9").append(String.format("%.2f", foodTax)).append("\n");
        billText.append("Local Tax: \u20B9").append(String.format("%.2f", localTax)).append("\n");
        billText.append("\n");
        billText.append("Total: \u20B9").append(String.format("%.2f", totalAmount)).append("\n");
        billText.append("----------------------------------------------------------------------\n");
        billText.append("\n");
        billText.append("                             THANK YOU");

        // Set the bill text to the TextView
        billTextView.setText(billText.toString());

        // Initialize Firebase Realtime Database
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://restrobill-f91e9-default-rtdb.asia-southeast1.firebasedatabase.app");
        billReference = database.getReference();

        saveButton = findViewById(R.id.buttonSave);
        double finalTotalAmount = totalAmount;
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBillDetails(foodItems, subtotal, foodTax, localTax, finalTotalAmount);
            }
        });

        // Print button click listener
        printButton = findViewById(R.id.buttonPrintBill);
        printButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generatePDF(billText.toString());
            }
        });

        cancelButton = findViewById(R.id.buttonClose);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the new activity or perform desired action
                Intent intent = new Intent(BillActivity.this, DashboardActivity.class);
                startActivity(intent);
            }
        });
    }

    private String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // Handle back button click here
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private static final int REQUEST_CODE_SAVE_PDF = 1;

    private void generatePDF(String billText) {
        // Create a new PDF document
        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        // Create a canvas for drawing the bill text
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(12);

        String[] lines = billText.split("\n");
        float y = 50;
        for (String line : lines) {
            canvas.drawText(line, 50, y, paint);
            y += paint.descent() - paint.ascent();
        }

        // Finish the page and save the document
        document.finishPage(page);
        File pdfFile = new File(getExternalFilesDir(null), "bill.pdf");

        try {
            document.writeTo(new FileOutputStream(pdfFile));
            Toast.makeText(this, "PDF saved: " + pdfFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();

            // Upload PDF to Firebase Storage
            uploadPDF(pdfFile);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to save PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        // Open the PDF file using an external PDF viewer app
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", pdfFile);
        intent.setDataAndType(uri, "application/pdf");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No PDF viewer app installed", Toast.LENGTH_SHORT).show();
        }

        // Close the document
        document.close();
    }

    private void uploadPDF(File pdfFile) {
        // Get the current user
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a unique filename for the PDF
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String filename = "bill_" + currentUser.getUid() + "_" + timestamp + ".pdf";

        // Upload the PDF file to Firebase Storage
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("bills").child(filename);
        UploadTask uploadTask = storageReference.putFile(Uri.fromFile(pdfFile));

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get the download URL of the uploaded file
//                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                            @Override
//                            public void onSuccess(Uri downloadUri) {
//                                // Save the download URL to the Firebase Realtime Database
//                                billReference.child(currentUser.getUid()).push().setValue(downloadUri.toString())
//                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                            @Override
//                                            public void onSuccess(Void aVoid) {
//                                                Toast.makeText(BillActivity.this, "PDF uploaded successfully", Toast.LENGTH_SHORT).show();
//                                            }
//                                        })
//                                        .addOnFailureListener(new OnFailureListener() {
//                                            @Override
//                                            public void onFailure(@NonNull Exception e) {
//                                                Toast.makeText(BillActivity.this, "Failed to upload PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                                            }
//                                        });
//                            }
//                        });
                        Toast.makeText(BillActivity.this, "PDF uploaded successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(BillActivity.this, "Failed to upload PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveBillDetails(List<FoodItem> foodItems, double subtotal, double foodTax, double localTax, double totalAmount) {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a unique key for the bill
        String billKey = billReference.child(currentUser.getUid()).push().getKey();

        // Create a Bill object to store the bill details
        Bill bill = new Bill(billKey, getCurrentDate(), foodItems, subtotal, foodTax, localTax, totalAmount);

        // Save the bill details to the Firebase Realtime Database
        billReference.child(currentUser.getUid()).child(billKey).setValue(bill)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(BillActivity.this, "Bill details saved successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(BillActivity.this, "Failed to save bill details: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
