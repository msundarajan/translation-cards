package org.mercycorps.translationcards.activity.addDeck;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;

import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.activity.addDeck.presenter.EnterDeckDestinationPresenter;

import butterknife.Bind;
import butterknife.OnClick;

public class EnterDeckDestinationLanguagesActivity extends AddDeckActivity implements EnterDeckDestinationPresenter.EnterDeckDestinationView {
    public static final int REQUEST_CODE = 0;
    private NewDeckContext newDeckContext;
    private EnterDeckDestinationPresenter presenter;

    @Bind(R.id.enter_destination_next_label)
    LinearLayout nextButton;
    @Bind(R.id.enter_destination_next_text)
    TextView nextButtonText;
    @Bind(R.id.enter_destination_next_image)
    ImageView nextButtonImage;
    @Bind(R.id.language_chip_flexbox)
    FlexboxLayout flexboxLayout;
    @Bind(R.id.add_language_button)
    TextView addLanguageButton;

    @Override
    public void inflateView() {
        setContentView(R.layout.activity_deck_destination_languages);
        newDeckContext = getContextFromIntent();
        presenter = new EnterDeckDestinationPresenter(this, newDeckContext);
    }

    @Override
    public void setBitmapsForActivity() {
        presenter.setBitmaps();
    }

    @Override
    public void initStates() {
        formatAddLanguageButton();
        presenter.updateNextButtonState();
        presenter.populateFlexBox();
        populateFlexBox();
    }

    private void formatAddLanguageButton() {
        BitmapDrawable img = (BitmapDrawable) ContextCompat.getDrawable(this, R.drawable.ic_control_point_white_24dp);
        if (img != null) {
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(img.getBitmap(), densityPixelsToPixels(20), densityPixelsToPixels(20), false);
            BitmapDrawable bitmapDrawable = new BitmapDrawable(this.getResources(), scaledBitmap);
            bitmapDrawable.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
            addLanguageButton.setCompoundDrawablesWithIntrinsicBounds(bitmapDrawable, null, null, null);
            addLanguageButton.setCompoundDrawablePadding(densityPixelsToPixels(5));
        }
    }

    private void populateFlexBox() {
        flexboxLayout.removeAllViews();
        for (String language : newDeckContext.getDestinationLanguages()) {
            flexboxLayout.addView(createLanguageChip(language));
        }
    }

    private TextView createLanguageChip(String language) {
        TextView chip = new TextView(this);
        chip.setText(language);
        chip.setTextColor(ContextCompat.getColor(this, R.color.textColor));
        chip.setTypeface(null, Typeface.BOLD);
        chip.setSingleLine();
        int fiveDp = densityPixelsToPixels(5);
        int tenDp = densityPixelsToPixels(10);
        FlexboxLayout.LayoutParams layoutParams = new FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(fiveDp, tenDp, fiveDp, 0);
        chip.setLayoutParams(layoutParams);
        chip.setPadding(tenDp, fiveDp, tenDp, fiveDp);
        chip.setBackgroundResource(R.drawable.language_chip_background);

        BitmapDrawable img = (BitmapDrawable) ContextCompat.getDrawable(this, R.drawable.ic_cancel_white_24dp);
        if (img != null) {
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(img.getBitmap(), densityPixelsToPixels(20), densityPixelsToPixels(20), false);
            BitmapDrawable bitmapDrawable = new BitmapDrawable(this.getResources(), scaledBitmap);
            chip.setCompoundDrawablesWithIntrinsicBounds(null, null, bitmapDrawable, null);
            chip.setCompoundDrawablePadding(tenDp);
        }

        chip.setOnTouchListener(createChipTouchListener());
        return chip;
    }

    @NonNull
    private View.OnTouchListener createChipTouchListener() {
        return new View.OnTouchListener() {
            boolean validEvent = true;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                TextView textView = (TextView) v;
                float yPos = event.getY();
                float xPos = event.getX();
                boolean validY = (yPos >= 0) && (yPos <= textView.getHeight());
                boolean validX = (xPos >= (textView.getWidth() - textView.getTotalPaddingRight())) && (xPos <= textView.getWidth());
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        validEvent = validX;
                        break;
                    case MotionEvent.ACTION_UP:
                        if (validEvent) {
                            removeLanguage(textView.getText().toString());
                        }
                        validEvent = false;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        validEvent = validY && validX;
                        break;
                    default:
                        break;
                }

                return true;
            }
        };
    }

    private void removeLanguage(String language) {
        newDeckContext.getDestinationLanguages().remove(language);
        populateFlexBox();
        presenter.updateNextButtonState();
    }

    @OnClick(R.id.enter_destination_next_label)
    public void nextButtonClicked() {
        if (!nextButton.isClickable()) {
            return;
        }

        startNextActivity(EnterDeckDestinationLanguagesActivity.this, EnterAuthorActivity.class);
    }

    @OnClick(R.id.enter_destination_back_arrow)
    public void backButtonClicked() {
        startNextActivity(this, EnterDeckSourceLanguageActivity.class);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            String selectedLanguage = data.getStringExtra(LanguageSelectorActivity.SELECTED_LANGUAGE_KEY);
            if (selectedLanguage != null) {
                newDeckContext.addDestinationLanguage(selectedLanguage);
                populateFlexBox();
            }
            presenter.updateNextButtonState();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.add_language_button)
    public void showLanguageSelector() {
        startActivityForResult(new Intent(this, LanguageSelectorActivity.class), REQUEST_CODE);
    }

    @Override
    public void updateNextButton(boolean clickable, int buttonColor, int backgroundResource) {
        nextButton.setClickable(clickable);
        nextButtonText.setTextColor(ContextCompat.getColor(this, buttonColor));
        nextButtonImage.setBackgroundResource(backgroundResource);
    }
}
