package com.project.android.bakingapp;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.project.android.bakingapp.data.RecipeContentProvider;
import com.project.android.bakingapp.data.RecipeContract;
import com.project.android.bakingapp.data.RecipeDbHelper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;

/**
 * Created by fg7cpt on 2/13/2018.
 */

@RunWith(AndroidJUnit4.class)
public class TestRecipeContentProvider {

    private final Context mContext = InstrumentationRegistry.getTargetContext();

    @Before
    public void setUp() {

        RecipeDbHelper dbHelper = new RecipeDbHelper(mContext);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.delete(RecipeContract.RecipeEntry.TABLE_NAME, null, null);
        database.delete(RecipeContract.RecipeIngredients.TABLE_NAME, null, null);
        database.delete(RecipeContract.RecipeSteps.TABLE_NAME, null, null);
    }

    @Test
    public void testProviderRegistry() {
        String packageName = mContext.getPackageName();
        String recipeProviderClassName = RecipeContentProvider.class.getName();
        ComponentName componentName = new ComponentName(packageName, recipeProviderClassName);

        try {

            PackageManager packageManager = mContext.getPackageManager();
            ProviderInfo providerInfo = packageManager.getProviderInfo(componentName, 0);
            String actualAuthority = providerInfo.authority;
            String expectedAuthority = packageName;

            String incorrectAuthority = "Error: RecipeContentProvider registered with authority: \" + actualAuthority +\n" +
                    "\" instead of expected authority: " + expectedAuthority;

            assertEquals(incorrectAuthority,
                    actualAuthority,
                    expectedAuthority);
        } catch (PackageManager.NameNotFoundException e) {
            String providerNotRegistredAtAll = "";


        }

    }

}
