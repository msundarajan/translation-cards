package org.mercycorps.translationcards.service;

import org.junit.Before;
import org.junit.Test;
import org.mercycorps.translationcards.data.Dictionary;
import org.mercycorps.translationcards.data.Repository;
import org.mercycorps.translationcards.data.Translation;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class TranslationServiceTest {

    @Mock
    Repository repository;
    @Mock
    DictionaryService dictionaryService;

    Translation defaultTranslation;
    Translation noAudioTranslation;
    List<Translation> translationsFromRepository;

    TranslationService translationService;

    @Before
    public void setup() {
        initMocks(this);

        defaultTranslation = new Translation("label", true, "filename", 1l, "translated text");
        noAudioTranslation = new Translation("no audio label", true, "", 2l, "no audio translated text");
        translationsFromRepository = Arrays.asList(defaultTranslation, noAudioTranslation);

        when(repository.getTranslationsForDictionary(any(Dictionary.class))).thenReturn(translationsFromRepository);

        translationService = new TranslationService(repository, dictionaryService);
    }

    @Test
    public void getCurrentTranslations_shouldReturnAllCardsByDefault() {
        List<Translation> translations = translationService.getCurrentTranslations();

        assertEquals(translationsFromRepository, translations);
    }

    @Test
    public void getCurrentTranslations_shouldReturnAllCardsWhenToggleIsSetToTrue() {
        translationService.setDisplayCardsWithNoAudio(true);
        List<Translation> translations = translationService.getCurrentTranslations();

        assertEquals(translationsFromRepository, translations);
    }

    @Test
    public void getCurrentTranslations_shouldOnlyReturnCardsWithAudioWhenToggleIsSetToFalse() {
        translationService.setDisplayCardsWithNoAudio(false);
        List<Translation> translations = translationService.getCurrentTranslations();

        assertEquals(Collections.singletonList(defaultTranslation), translations);
    }

    @Test
    public void deleteTranslation_shouldDeleteTranslationsBySourcePhraseFromCurrentDictionaries() {
        List<Dictionary> dictionaries = Collections.emptyList();
        when(dictionaryService.getDictionariesForCurrentDeck()).thenReturn(dictionaries);

        translationService.deleteTranslation("source phrase");

        verify(repository).deleteTranslationBySourcePhrase("source phrase", dictionaries);
    }

    @Test
    public void allTranslationsShouldBeMinimizedByDefault() {
        for(int index = 0; index < translationService.getCurrentTranslations().size(); index++) {
            assertEquals(false, translationService.cardIsExpanded(index));
        }
    }

    @Test
    public void expandCard_shouldExpandACard() {
        translationService.expandCard(1);
        assertEquals(true, translationService.cardIsExpanded(1));
    }

    @Test
    public void minimizeCard_shouldMinimizeACard() {
        translationService.expandCard(1);
        translationService.minimizeCard(1);
        assertEquals(false, translationService.cardIsExpanded(1));
    }
}