package com.conquer.exoplayer.core.trackselection;

import com.conquer.exoplayer.core.ExoPlayer;
import com.conquer.exoplayer.core.Renderer;
/**
 * The component of an {@link ExoPlayer} responsible for selecting tracks to be consumed(被消费) by each of
 * the player's {@link Renderer}s. The {@link DefaultTrackSelector} implementation should be
 * suitable for most use cases.
 *
 * <h3>Interactions(互动) with the player</h3>
 *
 * The following interactions occur between the player and its track selector during playback(回放).
 *
 * <p>
 *
 * <ul>
 *   <li>When the player is created it will initialize the track selector by calling {@link
 *       #init(InvalidationListener, BandwidthMeter)}.
 *   <li>When the player needs to make a track selection it will call {@link
 *       #selectTracks(RendererCapabilities[], TrackGroupArray)}. This typically occurs at the start
 *       of playback, when the player starts to buffer a new period of the media being played, and
 *       when the track selector invalidates its previous selections.
 *   <li>The player may perform a track selection well in advance of the selected tracks becoming
 *       active, where active is defined to mean that the renderers are actually consuming media
 *       corresponding to the selection that was made. For example when playing media containing
 *       multiple periods, the track selection for a period is made when the player starts to buffer
 *       that period. Hence if the player's buffering policy is to maintain a 30 second buffer, the
 *       selection will occur approximately 30 seconds in advance of it becoming active. In fact the
 *       selection may never become active, for example if the user seeks to some other period of
 *       the media during the 30 second gap. The player indicates to the track selector when a
 *       selection it has previously made becomes active by calling {@link
 *       #onSelectionActivated(Object)}.
 *   <li>If the track selector wishes to indicate to the player that selections it has previously
 *       made are invalid, it can do so by calling {@link
 *       InvalidationListener#onTrackSelectionsInvalidated()} on the {@link InvalidationListener}
 *       that was passed to {@link #init(InvalidationListener, BandwidthMeter)}. A track selector
 *       may wish to do this if its configuration has changed, for example if it now wishes to
 *       prefer audio tracks in a particular language. This will trigger the player to make new
 *       track selections. Note that the player will have to re-buffer in the case that the new
 *       track selection for the currently playing period differs from the one that was invalidated.
 * </ul>
 *
 * <h3>Renderer configuration(渲染器配置)</h3>
 *
 * The {@link TrackSelectorResult} returned by {@link #selectTracks(RendererCapabilities[],
 * TrackGroupArray)} contains not only {@link TrackSelection}s for each renderer, but also {@link
 * RendererConfiguration}s defining configuration parameters that the renderers should apply when
 * consuming the corresponding media. Whilst it may seem counter-intuitive for a track selector to
 * also specify renderer configuration information, in practice the two are tightly bound together.
 * It may only be possible to play a certain combination tracks if the renderers are configured in a
 * particular way. Equally, it may only be possible to configure renderers in a particular way if
 * certain tracks are selected. Hence it makes sense to determined the track selection and
 * corresponding renderer configurations in a single step.
 *
 * <h3>Threading model</h3>
 *
 * All calls made by the player into the track selector are on the player's internal playback
 * thread. The track selector may call {@link InvalidationListener#onTrackSelectionsInvalidated()}
 * from any thread.
 */
public abstract class TrackSelector {

}
