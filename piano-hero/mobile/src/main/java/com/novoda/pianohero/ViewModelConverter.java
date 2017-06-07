package com.novoda.pianohero;

import java.util.Locale;

class ViewModelConverter {

    private static final String NEXT_NOTE_HINT_FORMAT = "Next: %s";
    private static final String SHARP_SYMBOL = "#";

    private final SimplePitchNotationFormatter pitchNotationFormatter;

    ViewModelConverter(SimplePitchNotationFormatter pitchNotationFormatter) {
        this.pitchNotationFormatter = pitchNotationFormatter;
    }

    GameStartViewModel createGameStartViewModel(Sequence sequence) {
        String message = "Let's start!";

        return new GameStartViewModel(
            sequence,
            message
        );
    }

    ClockViewModel createClockViewModel(long secondsLeft) {
        return new ClockViewModel(secondsLeft + "s");
    }

    SongStartViewModel createSongStartViewModel(Note currentNote, Note nextNote) {
        return new SongStartViewModel(
            currentNoteFormatted(currentNote),
            nextNoteFormatted(nextNote)
        );
    }

    private String currentNoteFormatted(Note note) {
        return pitchNotationFormatter.format(note);
    }

    private String nextNoteFormatted(Note note) {
        String nextNote = pitchNotationFormatter.format(note);
        return String.format(Locale.US, NEXT_NOTE_HINT_FORMAT, nextNote);
    }

    RoundEndViewModel createRoundEndViewModel(
        Sequence sequence,
        Note currentNote,
        Note nextNote) {
        String successMessage = getSuccessMessage(sequence);

        return new RoundEndViewModel(
            sequence,
            currentNoteFormatted(currentNote),
            nextNoteFormatted(nextNote),
            successMessage
        );
    }

    private String getSuccessMessage(Sequence sequence) {
        if (sequence.position() > 0) {
            return String.format(Locale.US, "Woo! Keep going! (%d/%d)", sequence.position() + 1, sequence.length());
        } else {
            return "";
        }
    }

    RoundSuccessViewModel createSuccessViewModel(int score) {
        return new RoundSuccessViewModel(score);
    }

    RoundErrorViewModel createErrorViewModel(Sequence sequence, int score) {
        String errorMessage = getErrorMessage(sequence);

        Note errorNote = sequence.latestError();
        boolean isSharpError = pitchNotationFormatter.format(errorNote).endsWith(SHARP_SYMBOL);

        return new RoundErrorViewModel(
            sequence,
            errorMessage,
            isSharpError,
            score
        );
    }

    private String getErrorMessage(Sequence sequence) {
        String nextNoteAsText = pitchNotationFormatter.format(sequence.get(sequence.position()));
        String latestErrorAsText = pitchNotationFormatter.format(sequence.latestError());
        return String.format(Locale.US, "Ruhroh! The correct note is %s but you played %s.", nextNoteAsText, latestErrorAsText);
    }

    GameOverViewModel createGameOverViewModel() {
        return new GameOverViewModel("GAME OVER");
    }
}
