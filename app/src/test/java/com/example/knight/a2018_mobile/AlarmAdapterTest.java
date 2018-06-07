package com.example.knight.a2018_mobile;

import android.content.Context;
import android.test.mock.MockContext;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by leejisung on 2018-06-07.
 */
public class AlarmAdapterTest {

    Context context = new MockContext();
    AlarmAdapter test = new AlarmAdapter(context);

    @Test
    public void getCount() throws Exception {

        int length;
        length = test.getCount();
        assertTrue(length > 0);
        assertNotNull(length);

    }

    @Test
    public void getItem() throws Exception {
        Object obj;
        obj = test.getItem(0);
        assertNotNull(obj);
        obj = test.getItem(1);
        assertNotNull(obj);
        obj = test.getItem(2);
        assertNotNull(obj);
        obj = test.getItem(3);
        assertNotNull(obj);
        obj = test.getItem(4);
        assertNotNull(obj);

    }

}