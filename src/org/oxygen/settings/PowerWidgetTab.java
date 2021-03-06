package org.oxygen.settings;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.ListView;

public class PowerWidgetTab extends PreferenceActivity {

    private static final String KEY_EXP_WIDGET = "expanded_widget";
    private static final String KEY_EXP_WIDGET_HIDE_ONCHANGE = "expanded_hide_onchange";
    private static final String KEY_EXP_WIDGET_HIDE_SCROLLBAR = "expanded_hide_scrollbar";
    private static final String KEY_EXP_WIDGET_COLOR = "expanded_color_mask";
    private static final String KEY_EXP_WIDGET_PICKER = "widget_picker";
    private static final String KEY_EXP_WIDGET_ORDER = "widget_order";

    private CheckBoxPreference mPowerWidget;
    private CheckBoxPreference mPowerWidgetHideOnChange;
    private CheckBoxPreference mPowerWidgetHideScrollBar;
    private Preference mPowerWidgetColor;
    private PreferenceScreen mPowerPicker;
    private PreferenceScreen mPowerOrder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.tab_powerwidget);

        final PreferenceScreen prefSet = getPreferenceScreen();

        mPowerWidget = (CheckBoxPreference) prefSet.findPreference(KEY_EXP_WIDGET);
        mPowerWidget.setChecked((Settings.System.getInt(getContentResolver(),
            Settings.System.EXPANDED_VIEW_WIDGET, 0) == 1));
        mPowerWidgetHideOnChange = (CheckBoxPreference) prefSet.findPreference(KEY_EXP_WIDGET_HIDE_ONCHANGE);
        mPowerWidgetHideOnChange.setChecked((Settings.System.getInt(getContentResolver(),
            Settings.System.EXPANDED_HIDE_ONCHANGE, 0) == 1));
        mPowerWidgetHideScrollBar = (CheckBoxPreference) prefSet.findPreference(KEY_EXP_WIDGET_HIDE_SCROLLBAR);
        mPowerWidgetHideScrollBar.setChecked((Settings.System.getInt(getContentResolver(),
            Settings.System.EXPANDED_HIDE_SCROLLBAR, 1) == 1));
        mPowerWidgetColor = prefSet.findPreference(KEY_EXP_WIDGET_COLOR);
        mPowerPicker = (PreferenceScreen)prefSet.findPreference(KEY_EXP_WIDGET_PICKER);
        mPowerOrder = (PreferenceScreen) prefSet.findPreference(KEY_EXP_WIDGET_ORDER);

        // Start animation
        AnimationSet set = new AnimationSet(true);

        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(50);
        set.addAnimation(animation);

        animation = new TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0.0f,Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, -1.0f,Animation.RELATIVE_TO_SELF, 0.0f
        );
        animation.setDuration(100);
        set.addAnimation(animation);

        LayoutAnimationController controller = new LayoutAnimationController(set, 0.5f);
        ListView listView = getListView();
        listView.setLayoutAnimation(controller);
        // End animation
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        boolean value;
        if (preference == mPowerWidget) {
            value = mPowerWidget.isChecked();
            Settings.System.putInt(getContentResolver(),
                Settings.System.EXPANDED_VIEW_WIDGET, value ? 1 : 0);
        } else if (preference == mPowerWidgetHideOnChange) {
            value = mPowerWidgetHideOnChange.isChecked();
            Settings.System.putInt(getContentResolver(),
                Settings.System.EXPANDED_HIDE_ONCHANGE, value ? 1 : 0);
        } else if (preference == mPowerWidgetHideScrollBar) {
            value = mPowerWidgetHideScrollBar.isChecked();
            Settings.System.putInt(getContentResolver(),
                Settings.System.EXPANDED_HIDE_SCROLLBAR, value ? 1 : 0);
        } else if (preference == mPowerWidgetColor) {
            ColorPickerDialog cp = new ColorPickerDialog(this,
                mWidgetColorListener,
                readWidgetColor());
            cp.show();
        } else if (preference == mPowerPicker) {
            startActivity(mPowerPicker.getIntent());
        } else if (preference == mPowerOrder) {
            startActivity(mPowerOrder.getIntent());
        }
        return true;
    }

    private int readWidgetColor() {
        try {
            return Settings.System.getInt(getContentResolver(), Settings.System.EXPANDED_VIEW_WIDGET_COLOR);
        }
        catch (SettingNotFoundException e) {
            return -16777216;
        }
    }

    ColorPickerDialog.OnColorChangedListener mWidgetColorListener =
        new ColorPickerDialog.OnColorChangedListener() {
            public void colorChanged(int color) {
                Settings.System.putInt(getContentResolver(), Settings.System.EXPANDED_VIEW_WIDGET_COLOR, color);
            }
            public void colorUpdate(int color) {
            }
        };

}
