package com.attendance.tuesbae.tuesbaeattendance;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static com.attendance.tuesbae.tuesbaeattendance.User.UserNameComparator;

public class MainActivity extends AppCompatActivity {

    List<User> UserList = new ArrayList<>();
    List<User> AttendanceUserList = new ArrayList<>();

    UserListAdapter UserListAdapter;
    ListView user_listView;
    Button attendance_date_selected;

    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener date;
    FirebaseDatabase database;

   ArrayAdapter<String> addPlayerArrayAdapter;
    String myFormat = "EEEE, dd-MM-yyyy"; //In which you need put here
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user_listView = (ListView) findViewById(R.id.userlistview);


       database = FirebaseDatabase.getInstance();
       // DatabaseReference myRef = database.getReference().child("Vegetables").child("Tomato");




        attendance_date_selected= (Button) findViewById(R.id.attendance_date_selected);


        attendance_date_selected.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub


                ConnectivityManager conMgr =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

                if(netInfo!=null) {
                    initializeOnDateSetListener();

                    new DatePickerDialog(MainActivity.this, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
                else{
                    Toast.makeText(MainActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
                }

            }
        });

//


        final DatabaseReference myRef = database.getReference("User/ID");

        // Read from the database
       myRef.addValueEventListener(new ValueEventListener() {

           @Override
           public void onDataChange(final DataSnapshot dataSnapshot) {

               //Toast.makeText(MainActivity.this,"Value is: "+dataSnapshot+" instance is: "+FirebaseDatabase.getInstance().toString(),Toast.LENGTH_SHORT).show();


               if(!UserList.isEmpty()){
                   UserList.clear();
               }

                        for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren() ){
                            // Iterate process here
                            User user = new User(dataSnapshot1.child("Name").getValue().toString(),
                                    dataSnapshot1.child("Attended Game").getValue().toString());


                            UserList.add(user);
                            Collections.sort(UserList,UserNameComparator);



                            // Toast.makeText(MainActivity.this,"Value is: "+user.getAlt_Name().toString(),Toast.LENGTH_SHORT).show();

//                   Toast.makeText(MainActivity.this,"Value is: "+dataSnapshot1.child("Name").getValue()+" instance is: "+FirebaseDatabase.getInstance().toString(),Toast.LENGTH_SHORT).show();

                        }




//               for(int j=0;j<UserList.size();j++) {
//                   Toast.makeText(MainActivity.this, "Namez: "+UserList.get(j).getName(), Toast.LENGTH_SHORT).show();
//               }



           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       Toast.makeText(MainActivity.this,"Fail to load data",Toast.LENGTH_SHORT).show();

                   }
               });



           }




       });



       user_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
               final TextView tv_Name = (TextView) view.findViewById(R.id.userlist_name_textview);
               final TextView tv_attending = (TextView) view.findViewById(R.id.userlist_status_textview);

               //myRef.child((position+1)+"").child("Attended Game").setValue(String.valueOf(Integer.valueOf(UserList.get(position).getGames_Attended())+1));

               final DatabaseReference myDateRef1 = database.getReference("Game Session/Date/" + sdf.format(myCalendar.getTime())+"/"+tv_Name.getText().toString());

               myDateRef1.addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(DataSnapshot dataSnapshot) {

                       if(dataSnapshot.getKey().equals("No Attendance")){

                           Toast.makeText(getBaseContext(),"Please add a player",Toast.LENGTH_SHORT).show();

                       }
                       else{
                           if (!dataSnapshot.getValue().equals("Paid")) {
                               myDateRef1.setValue("Paid");

                               AttendanceUserList.get(position).setStatus("Paid");
                               UserListAdapter.notifyDataSetChanged();


                           } else {
                               myDateRef1.setValue("Attending");

                               AttendanceUserList.get(position).setStatus("Attending");
                               UserListAdapter.notifyDataSetChanged();
                           }
                       }
                   }

                   @Override
                   public void onCancelled(DatabaseError databaseError) {


                   }
               });

           }
       });

        user_listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                createConfirmDeleteUserDialog(view,position);

                return true;
            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            createUserListDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void updateLabel() {

        attendance_date_selected.setText(sdf.format(myCalendar.getTime()));
    }

    public void createUserListDialog(){
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(MainActivity.this);
        builderSingle.setTitle("Select A Player:");

        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        addPlayerArrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1);


        for(int i=0; i<UserList.size();i++){
            addPlayerArrayAdapter.add(UserList.get(i).getName());
        }




        if(date!=null) {
            builderSingle.setAdapter(addPlayerArrayAdapter, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {


                    final String strName = addPlayerArrayAdapter.getItem(which);

                    final DatabaseReference myDateRef = database.getReference("Game Session/Date/" + sdf.format(myCalendar.getTime()));



                    if(!AttendanceUserList.get(0).getName().equals("No Attendance")) {


                        myDateRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {


                              if(!dataSnapshot.hasChild(strName)){
                                  myDateRef.child(strName).setValue("Attending");

                                  User user = new User(strName, "Attending");

                                  AttendanceUserList.add(user);

                                  Collections.sort(AttendanceUserList, UserNameComparator);
                                  UserListAdapter = new UserListAdapter(MainActivity.this, AttendanceUserList);
                                  UserListAdapter.notifyDataSetChanged();
                                  user_listView.setAdapter(UserListAdapter);
                              }
                              else{
                                  Toast.makeText(MainActivity.this,"Player has already been added to the Attendance List",Toast.LENGTH_SHORT).show();
                              }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                    else{
                        myDateRef.child("No Attendance").removeValue();

                        myDateRef.child(strName).setValue("Attending");
                        User user = new User(strName, "Attending");


                        AttendanceUserList.set(0,user);
                        UserListAdapter = new UserListAdapter(MainActivity.this, AttendanceUserList);
                        UserListAdapter.notifyDataSetChanged();
                        user_listView.setAdapter(UserListAdapter);
                    }


                }
            });
            builderSingle.show();
        }
        else{
            Toast.makeText(MainActivity.this,"Date not set, please set a date",Toast.LENGTH_SHORT).show();
        }
    }

    public void createConfirmDeleteUserDialog(final View view, final int position) {

        final TextView tv_Name = (TextView) view.findViewById(R.id.userlist_name_textview);
        final TextView tv_attending = (TextView) view.findViewById(R.id.userlist_status_textview);

        String boldText = tv_Name.getText().toString();
        String normalText = "Remove ";
        String normalText2 = " from Attendance List? ";
        SpannableString str = new SpannableString(normalText+boldText+normalText2);
        str.setSpan(new StyleSpan(Typeface.BOLD), normalText.length(), normalText.length()+boldText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);



        AlertDialog.Builder builderSingle = new AlertDialog.Builder(MainActivity.this,R.style.CustomAlertDialogTheme);
        builderSingle.setMessage(str);



        builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();



                //myRef.child((position+1)+"").child("Attended Game").setValue(String.valueOf(Integer.valueOf(UserList.get(position).getGames_Attended())+1));

                final DatabaseReference myDateRefA = database.getReference("Game Session/Date/" + sdf.format(myCalendar.getTime()));


                final DatabaseReference myDateRef1 = database.getReference("Game Session/Date/" + sdf.format(myCalendar.getTime())+"/"+tv_Name.getText().toString());

                myDateRefA.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.getChildrenCount()>1) {

                            myDateRef1.removeValue();

                            AttendanceUserList.remove(position);
                            UserListAdapter.notifyDataSetChanged();

                        }
                        else{

                            myDateRef1.removeValue();

                            myDateRefA.child("No Attendance").setValue("-");
                            User user = new User("No Attendance", "-");


                            AttendanceUserList.set(0,user);
                            UserListAdapter.notifyDataSetChanged();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });




            }
        }).show();



    }

    public void initializeOnDateSetListener(){
        date = new DatePickerDialog.OnDateSetListener() {



            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);



                updateLabel();




                final DatabaseReference myRef = database.getReference("Game Session/Date");

                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(sdf.format(myCalendar.getTime())).exists()){

                            if(!AttendanceUserList.isEmpty()){
                                AttendanceUserList.clear();
                            }


                            for(DataSnapshot dataSnapshot1 : dataSnapshot.child(sdf.format(myCalendar.getTime())).getChildren() ){
                                // Iterate process here



                                User user = new User(dataSnapshot1.getKey(), dataSnapshot1.getValue().toString());

                                AttendanceUserList.add(user);
                                UserListAdapter = new UserListAdapter(MainActivity.this,AttendanceUserList);
                                UserListAdapter.notifyDataSetChanged();
                                user_listView.setAdapter(UserListAdapter);


                            }
                        }
                        else{

                            if(!AttendanceUserList.isEmpty()){
                                AttendanceUserList.clear();
                            }

                            myRef.child(sdf.format(myCalendar.getTime())).child("No Attendance").setValue("-");

                            User user = new User("No Attendance", "-");

                            AttendanceUserList.add(user);
                            UserListAdapter = new UserListAdapter(MainActivity.this,AttendanceUserList);
                            UserListAdapter.notifyDataSetChanged();
                            user_listView.setAdapter(UserListAdapter);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

//                User user = new User ("z", "alt namez","attendz");
//
//                myRef.child("Richardz").setValue("Paid");



            }

        };
    }

    public void checkinternetconnectionForFireBase(){
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
//        connectedRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                boolean connected = dataSnapshot.getValue(Boolean.class);
//
//                if(connected) {
//                    Toast.makeText(MainActivity.this, "Connected", Toast.LENGTH_SHORT).show();
//                }
//                else{
//                    Toast.makeText(MainActivity.this, "Fail to Connect", Toast.LENGTH_SHORT).show();
//
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }
}
