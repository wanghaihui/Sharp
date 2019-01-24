package com.conquer.sharp.guide.core;

import android.view.View;

import com.conquer.sharp.guide.listener.OnHighlightDrawListener;

public class HighlightOptions {

    public View.OnClickListener onClickListener;
    public OnHighlightDrawListener onHighlightDrawListener;
    public RelativeGuide relativeGuide;
    public boolean fetchLocationEveryTime;

    public static class Builder {

        private HighlightOptions options;

        public Builder() {
            options = new HighlightOptions();
        }

        public Builder setRelativeGuide(RelativeGuide relativeGuide) {
            options.relativeGuide = relativeGuide;
            return this;
        }

        public HighlightOptions build() {
            return options;
        }
    }
}
