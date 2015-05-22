package com.example.heart;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import java.util.Calendar;
import java.util.regex.Pattern;
import java.text.DateFormat;

public class MainActivity extends Activity {

	DateFormat formate = DateFormat.getDateInstance();
	Calendar calendar;
	TextView labelDate, labelNumber, labelName, labelError;
	Button dateButton, contactButton, saveButton;
	String chosenDate = "Wybrano datê: ";
	String chosenNumber = "Wybrano numer: ";
	static final int PICK_CONTACT_REQUEST = 1;
	User user;
	String alertNumber;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		labelDate = (TextView)findViewById(R.id.textChosenDate);
		labelNumber = (TextView)findViewById(R.id.textChosenNumber);
		labelError = (TextView)findViewById(R.id.textError);
		dateButton = (Button)findViewById(R.id.buttonDate);
		dateButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				calendar = Calendar.getInstance();
				setDate();
			}
		});
		
		//dateButton.setOnClickListener(this);
		//updateDate();
		
		contactButton = (Button)findViewById(R.id.buttonAlertNumber);
		contactButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				pickContact();
			}
		});
		
		saveButton = (Button)findViewById(R.id.buttonSave);
		saveButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				labelName = (TextView)findViewById(R.id.editTextName);
				String name = labelName.getText().toString();
				if(name == null || name.equals("") || calendar == null || alertNumber == null || alertNumber.equals(""))
				{
					labelError.setText("Nale¿y wype³niæ wszystkie dane.");
				}
				else
				{
					if(Pattern.matches("[A-Z]{1}[a-z]+[ ][A-Z]{1}[a-z]+", name))
					{
						int age = countAge(calendar);
						if(age <= 0 )
						{
							labelError.setText("Data nie mo¿e byæ póŸniejsza ni¿ dzisiejsza.");
						}
						else	
						{	
							RadioGroup myRadio = (RadioGroup)findViewById(R.id.radioGroupSex);
					    	int selected = myRadio.getCheckedRadioButtonId();
					    	RadioButton rb = (RadioButton) findViewById(selected);
					    	String sex = rb.getText().toString();		
							user = new User(name, calendar, sex, alertNumber);
							labelError.setText("Zapisano u¿ytkownika.");
						}
					}
					else
					{
						labelError.setText("Imiê i nazwisko jest w z³ym formacie.");
					}
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void updateDate() {
		labelDate.setText(chosenDate + formate.format(calendar.getTime()));
	}
	
	public void setDate() {
		new DatePickerDialog(MainActivity.this, d, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
	}
	
	DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int month, int day) {
			calendar.set(Calendar.YEAR, year);
			calendar.set(Calendar.MONTH, month);
			calendar.set(Calendar.DAY_OF_MONTH, day);
			updateDate();
		}
	};
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void pickContact() {
	    Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
	    pickContactIntent.setType(Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
	    startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    // Check which request it is that we're responding to
	    if (requestCode == PICK_CONTACT_REQUEST) {
	        // Make sure the request was successful
	        if (resultCode == RESULT_OK) {
	            // Get the URI that points to the selected contact
	            Uri contactUri = data.getData();
	            // We only need the NUMBER column, because there will be only one row in the result
	            String[] projection = {Phone.NUMBER};

	            // Perform the query on the contact to get the NUMBER column
	            // We don't need a selection or sort order (there's only one result for the given URI)
	            // CAUTION: The query() method should be called from a separate thread to avoid blocking
	            // your app's UI thread. (For simplicity of the sample, this code doesn't do that.)
	            // Consider using CursorLoader to perform the query.
	            Cursor cursor = getContentResolver()
	                    .query(contactUri, projection, null, null, null);
	            cursor.moveToFirst();

	            // Retrieve the phone number from the NUMBER column
	            int column = cursor.getColumnIndex(Phone.NUMBER);
	            alertNumber = cursor.getString(column);
	            labelNumber.setText(chosenNumber + alertNumber);
	            // Do something with the phone number...
	        }
	    }
	}
	
	private int countAge(Calendar dob) {
		Calendar today = Calendar.getInstance();
		int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);  
		if (today.get(Calendar.MONTH) < dob.get(Calendar.MONTH)) {
		  age--;
		} else if (today.get(Calendar.MONTH) == dob.get(Calendar.MONTH) && today.get(Calendar.DAY_OF_MONTH) < dob.get(Calendar.DAY_OF_MONTH)) {
		  age--;
		}
		return age;
	}
}
