package com.dcnproject.yashdani.chipin;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aman on 29/3/18.
 */

public class Queries extends Thread {
List<String> Users_group_list = new ArrayList<>();
List<String> GUID_List = new ArrayList<>();
List<String> Groups_User_List = new ArrayList<>();
List<String> Groups_Transactions = new ArrayList<>();
List<String> Users_Transactions = new ArrayList<>( );

private DatabaseReference mDatabaseUsrRef, mDatabaseGrpRef, mDatabaseTransRef;
private int choice,check = 0;
private boolean first = true;
private boolean request = false;
private String Group_ID,User_ID;

    public int getCheck() {
        return check;
    }

    public void setCheck(int check) {
        this.check = check;
    }

    public boolean isRequest() {
        return request;
    }

    public void setRequest(boolean request) {
        this.request = request;
    }

    public List<String> getUsers_group_list() {
        return Users_group_list;
    }

    public void setUsers_group_list(List<String> users_group_list) {
        Users_group_list = users_group_list;
    }

    public List<String> getGUID_List() {
        return GUID_List;
    }

    public void setGUID_List(List<String> GUID_List) {
        this.GUID_List = GUID_List;
    }

    public List<String> getGroups_User_List() {
        return Groups_User_List;
    }

    public void setGroups_User_List(List<String> groups_User_List) {
        Groups_User_List = groups_User_List;
    }

    public List<String> getGroups_Transactions() {
        return Groups_Transactions;
    }

    public void setGroups_Transactions(List<String> groups_Transactions) {
        Groups_Transactions = groups_Transactions;
    }

    public List<String> getUsers_Transactions() {
        return Users_Transactions;
    }

    public void setUsers_Transactions(List<String> users_Transactions) {
        Users_Transactions = users_Transactions;
    }

    public int getChoice() {
        return choice;
    }

    public void setChoice(int choice) {
        this.choice = choice;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public String getGroup_ID() {
        return Group_ID;
    }

    public void setGroup_ID(String group_ID) {
        Group_ID = group_ID;
    }

    public String getUser_ID() {
        return User_ID;
    }

    public void setUser_ID(String user_ID) {
        User_ID = user_ID;
    }

    public void run(){
    mDatabaseUsrRef= FirebaseDatabase.getInstance().getReference().child("Users");
    mDatabaseGrpRef = FirebaseDatabase.getInstance().getReference().child("Groups");
    mDatabaseTransRef = FirebaseDatabase.getInstance().getReference().child("Transaction");


    if(first == true) {
        GUID_List = getGroupUIDs();
        first = false;
    }
    if(choice == 1 && GUID_List.size() > 0 && Group_ID != null)
        Groups_User_List = getGroupUsers_uid(Group_ID);
    if(choice == 2 && GUID_List.size()>0 && User_ID != null)
        Users_group_list = getUsersGroups_guid(User_ID);
    if(choice == 3 && GUID_List.size() > 0 && Group_ID != null)
        Groups_Transactions = getGroups_Transactions(Group_ID);
    if(choice == 4 && GUID_List.size() > 0 && User_ID != null)
        Groups_User_List = getGroupUsers_uid(Group_ID);
    if(choice == 5 && GUID_List.size() > 0 && User_ID != null)
        Users_Transactions = getUsers_Transactions(User_ID);
    request = false;
    check = GUID_List.size();

}

    public List<String> getGroupUIDs() {
    mDatabaseGrpRef.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot groups : dataSnapshot.getChildren())
                GUID_List.add(groups.getKey().toString());
            first = true;



        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });
    return GUID_List;
}
    public List<String> getGroupUsers_uid(String GUID) {
        mDatabaseGrpRef.child(GUID).child("Users").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot users : dataSnapshot.getChildren())
                    Groups_User_List.add(users.getKey().toString());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return Groups_User_List;
    }
    public List<String> getUsersGroups_guid(final String UID) {
        mDatabaseGrpRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot groups : dataSnapshot.getChildren()){
                    if(groups.child("Users").hasChild(UID))
                        Users_group_list.add(groups.getKey().toString());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return Users_group_list;
    }
    public List<String> getGroups_Transactions(final String GUID) {
        mDatabaseTransRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot trans : dataSnapshot.getChildren()) {
                    if(trans.hasChild(GUID))
                        Groups_Transactions.add(trans.getKey().toString());

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return Groups_Transactions;
    }
    public List<String> getUsers_Transactions(final String UID) {
        Users_Transactions = null;
        Users_group_list = getUsersGroups_guid(UID);
        for(int i = 0; i < Users_group_list.size(); i++){
            Groups_Transactions = getGroups_Transactions(Users_group_list.get(i));
            for(int j = 0;j < Groups_Transactions.size(); j++ ){
                Users_Transactions.add(Groups_Transactions.get(j));
            }

        }

            return Users_Transactions;
    }

}





