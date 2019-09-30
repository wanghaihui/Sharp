package com.conquer.sharp.keyboard.emoji.event;

public class EmojiClickEvent {
    public String emojiText;
    public CharSequence emojiSpanText;

    public EmojiClickEvent(String emojiText, CharSequence emojiSpanText) {
        this.emojiText = emojiText;
        this.emojiSpanText = emojiSpanText;
    }
}
