package com.coara.javatest;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParsingProblem;
import com.github.javaparser.ast.CompilationUnit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity {

    private static final int READ_REQUEST_CODE = 42;
    private static final int STORAGE_PERMISSION_CODE = 100;

    private EditText editor;
    private TextView lineNumbers;
    private Button openButton;
    private Button saveButton;
    private Button undoButton;
    private Button redoButton;
    private Button shareButton;
    private Uri fileUri;
    private String fileContent = "";
    private String lastContent = "";
    private String redoContent = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editor = findViewById(R.id.editor);
        lineNumbers = findViewById(R.id.lineNumbers);
        openButton = findViewById(R.id.openButton);
        saveButton = findViewById(R.id.saveButton);
        undoButton = findViewById(R.id.undoButton);
        redoButton = findViewById(R.id.redoButton);
        shareButton = findViewById(R.id.shareButton);

        lineNumbers.setMovementMethod(new ScrollingMovementMethod());
        updateLineNumbers();

        openButton.setOnClickListener(v -> openFile());
        saveButton.setOnClickListener(v -> saveFile());
        undoButton.setOnClickListener(v -> undo());
        redoButton.setOnClickListener(v -> redo());
        shareButton.setOnClickListener(v -> shareFile());

        editor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                lastContent = editor.getText().toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateLineNumbers();
                highlightSyntaxErrors(s.toString()); // 構文エラーをリアルタイムで強調表示
            }

            @Override
            public void afterTextChanged(Editable s) {
                redoContent = "";
            }
        });
    }

    private void updateLineNumbers() {
        String[] lines = editor.getText().toString().split("\n");
        StringBuilder numbers = new StringBuilder();
        for (int i = 1; i <= lines.length; i++) {
            numbers.append(i).append("\n");
        }
        lineNumbers.setText(numbers.toString());
    }

    private void undo() {
        redoContent = editor.getText().toString();
        editor.setText(lastContent);
    }

    private void redo() {
        if (!redoContent.isEmpty()) {
            lastContent = editor.getText().toString();
            editor.setText(redoContent);
        }
    }

    private void openFile() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    STORAGE_PERMISSION_CODE);
        } else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            startActivityForResult(intent, READ_REQUEST_CODE);
        }
    }

    private void saveFile() {
        if (fileUri == null) {
            Toast.makeText(this, "ファイルを選択してください", Toast.LENGTH_SHORT).show();
            return;
        }

        File directory = new File("/sdcard/javaeditor/");
        if (!directory.exists()) {
            directory.mkdirs(); // ディレクトリを作成
        }

        String fileName = "MyJavaFile";
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File savedFile = new File(directory, fileName + "_" + timestamp + ".java");

        try {
            FileOutputStream outputStream = new FileOutputStream(savedFile);
            outputStream.write(editor.getText().toString().getBytes());
            outputStream.close();
            Toast.makeText(this, "保存しました: " + savedFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "保存に失敗しました", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareFile() {
        if (fileUri == null) {
            Toast.makeText(this, "共有するファイルがありません", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, editor.getText().toString());
        startActivity(Intent.createChooser(shareIntent, "共有"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == READ_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                fileUri = data.getData();
                loadFile(fileUri);
            }
        }
    }

    private void loadFile(Uri uri) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getContentResolver().openInputStream(uri)));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            reader.close();
            fileContent = stringBuilder.toString();
            editor.setText(fileContent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "ファイルを開けません", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openFile();
            } else {
                Toast.makeText(this, "ストレージの権限が必要です", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // 構文エラーを検出してエラー箇所に赤線を引く処理
    private void highlightSyntaxErrors(String code) {
        try {
            // JavaParserを使ってコードを解析
            CompilationUnit compilationUnit = JavaParser.parse(code);
            List<ParsingProblem> problems = compilationUnit.getProblems();

            // SpannableStringを使ってエディタに赤線を引く
            SpannableString spannable = new SpannableString(code);

            // すべてのエラーを処理
            for (ParsingProblem problem : problems) {
                int startPos = problem.getLocation().get().getRange().get().getBegin().getByte();
                int endPos = problem.getLocation().get().getRange().get().getEnd().getByte();

                // エラー箇所に赤線を引く
                spannable.setSpan(new BackgroundColorSpan(getResources().getColor(android.R.color.holo_red_light)),
                        startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                // エラーの詳細をユーザーに表示
                showErrorDetails(problem);
            }

            editor.setText(spannable);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "構文解析エラー: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // エラーの詳細メッセージを表示
    private void showErrorDetails(ParsingProblem problem) {
        String errorMessage = "エラー: " + problem.getMessage();
        String location = "行: " + problem.getLocation().get().getBegin().getLine();
        Toast.makeText(this, errorMessage + "\n" + location, Toast.LENGTH_SHORT).show();
    }
}
