package com.conquer.exoplayer.core.trackselection;

/**
 * Base class for {@link TrackSelector}s that first establish(建立) a mapping between {@link TrackGroup}s
 * and {@link Renderer}s, and then from that mapping create a {@link TrackSelection} for each
 * renderer.
 */
public abstract class MappingTrackSelector extends TrackSelector {

}
