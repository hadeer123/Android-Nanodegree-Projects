package com.project.android.bakingapp;

import android.provider.BaseColumns;

import com.project.android.bakingapp.data.RecipeContract;

import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ContractClassUnitTest {
//    @Test
//    public void inner_class_exists throws Exception{
//        Class[] innerClasses = RecipeContract.class.getDeclaredClasses();
//        assertEquals("There should be 3 Inner classes inside the contract class", 3, innerClasses.length);
//    }

    @Test
    public void inner_class_type_correct() throws Exception {
        Class[] innerClasses = RecipeContract.class.getDeclaredClasses();
        assertEquals("Cannot find inner class to complete unit test ", 3, innerClasses.length);

        Class entryClass = innerClasses[0];
        assertTrue("Inner class should implement the BaseColumn interface", BaseColumns.class.isAssignableFrom(entryClass));
        assertTrue("Inner class should be final", Modifier.isFinal(entryClass.getModifiers()));
        assertTrue("Inner class should be static", Modifier.isStatic(entryClass.getModifiers()));

        Class ingredientsClass = innerClasses[1];
        assertTrue("Inner class should implement the BaseColumn interface", BaseColumns.class.isAssignableFrom(ingredientsClass));
        assertTrue("Inner class should be final", Modifier.isFinal(ingredientsClass.getModifiers()));
        assertTrue("Inner class should be static", Modifier.isStatic(ingredientsClass.getModifiers()));


        Class stepsClass = innerClasses[2];
        assertTrue("Inner class should implement the BaseColumn interface", BaseColumns.class.isAssignableFrom(stepsClass));
        assertTrue("Inner class should be final", Modifier.isFinal(stepsClass.getModifiers()));
        assertTrue("Inner class should be static", Modifier.isStatic(stepsClass.getModifiers()));
    }

    @Test
    public void inner_class_members_correct () throws  Exception {
        Field[] allFields;
        Class[] innerClasses = RecipeContract.class.getDeclaredClasses();
        assertEquals("Cannot find inner class to complete unit test", 3, innerClasses.length);
        Class entryClass = innerClasses[0];
        allFields = entryClass.getDeclaredFields();
        assertEquals("There should be exactly 6 String members in the inner class", 6, allFields.length);
        for (Field field : allFields) {
            assertTrue("All members in the contract class should be Strings", field.getType()==String.class);
            assertTrue("All members in the contract class should be final", Modifier.isFinal(field.getModifiers()));
            assertTrue("All members in the contract class should be static", Modifier.isStatic(field.getModifiers()));
        }

        Class ingredientsClass = innerClasses[1];
        allFields = ingredientsClass.getDeclaredFields();
        assertEquals("There should be exactly 4 String members in the inner class", 4, allFields.length);
        for (Field field : allFields) {
            assertTrue("All members in the contract class should be Strings", field.getType()==String.class);
            assertTrue("All members in the contract class should be final", Modifier.isFinal(field.getModifiers()));
            assertTrue("All members in the contract class should be static", Modifier.isStatic(field.getModifiers()));
        }

        Class stepsClass = innerClasses[2];
        allFields = stepsClass.getDeclaredFields();
        assertEquals("There should be exactly 6 String members in the inner class", 6, allFields.length);
        for (Field field : allFields) {
            assertTrue("All members in the contract class should be Strings", field.getType()==String.class);
            assertTrue("All members in the contract class should be final", Modifier.isFinal(field.getModifiers()));
            assertTrue("All members in the contract class should be static", Modifier.isStatic(field.getModifiers()));
        }

    }
}