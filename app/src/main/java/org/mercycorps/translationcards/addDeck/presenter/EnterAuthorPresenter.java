package org.mercycorps.translationcards.addDeck.presenter;

import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.addDeck.activity.EnterDeckDestinationLanguagesActivity;
import org.mercycorps.translationcards.addDeck.NewDeckContext;
import org.mercycorps.translationcards.addDeck.activity.ReviewAndSaveDeckActivity;

public class EnterAuthorPresenter {

    private EnterAuthorView activity;
    private NewDeckContext newDeckContext;

    public EnterAuthorPresenter(EnterAuthorView activity, NewDeckContext newDeckContext) {
        this.activity = activity;
        this.newDeckContext = newDeckContext;
    }

    public void inflateBitmaps() {
        activity.setActivityBitmap(R.id.deck_author_image, R.drawable.enter_phrase_image);
    }

    public void initDeckAuthorInput() {
        activity.updateDeckAuthorInput(newDeckContext.getAuthor());
    }

    public void nextButtonClicked() {
        if (newDeckContext.getAuthor().isEmpty()) return;
        activity.startActivityWithClass(ReviewAndSaveDeckActivity.class);
    }

    public void backButtonClicked() {
        activity.startActivityWithClass(EnterDeckDestinationLanguagesActivity.class);
    }

    public void deckAuthorInputChanged(String deckAuthor) {
        newDeckContext.setAuthor(deckAuthor);
        Integer nextButtonColor = deckAuthor.isEmpty() ? R.color.textDisabled : R.color.primaryTextColor;
        Integer nextButtonArrow = deckAuthor.isEmpty() ? R.drawable.forward_arrow_disabled : R.drawable.forward_arrow;
        activity.updateNextButtonClickable(!deckAuthor.isEmpty(), nextButtonColor, nextButtonArrow);
    }

    public interface EnterAuthorView extends AddDeckView {
        void updateDeckAuthorInput(String deckAuthor);
        void updateNextButtonClickable(boolean nextButtonClickable, int nextButtonColor, int nextButtonArrow);
    }
}
