// IMyAidl.aidl
package com.pivot.myandroiddemo;

// Declare any non-default types here with import statements

import com.pivot.myandroiddemo.Person;

interface IMyAidl {
    void addPerson(in Person person);
    List<Person> getPersonList();
}
