package com.conquer.exoplayer.core.trackselection;

/**
 * 缺省的轨道选择器(音频或者视频轨道)
 * A default {@link TrackSelector} suitable for most use cases. Track selections(选择的轨道) are made according
 * to configurable {@link Parameters}, which can be set by calling {@link
 * #setParameters(Parameters)}.
 *
 * <h3>Modifying parameters</h3>
 *
 * To modify only some aspects of the parameters currently used by a selector, it's possible to
 * obtain a {@link ParametersBuilder} initialized with the current {@link Parameters}. The desired
 * modifications can be made on the builder, and the resulting {@link Parameters} can then be built
 * and set on the selector. For example the following code modifies the parameters to restrict video
 * track selections to SD, and to select a German audio track if there is one:
 *
 * <pre>{@code
 * // Build on the current parameters.
 * Parameters currentParameters = trackSelector.getParameters();
 * // Build the resulting parameters.
 * Parameters newParameters = currentParameters
 *     .buildUpon()
 *     .setMaxVideoSizeSd()
 *     .setPreferredAudioLanguage("deu")
 *     .build();
 * // Set the new parameters.
 * trackSelector.setParameters(newParameters);
 * }</pre>
 *
 * Convenience methods and chaining allow this to be written more concisely as:
 *
 * <pre>{@code
 * trackSelector.setParameters(
 *     trackSelector
 *         .buildUponParameters()
 *         .setMaxVideoSizeSd()
 *         .setPreferredAudioLanguage("deu"));
 * }</pre>
 *
 * Selection {@link Parameters} support many different options, some of which are described below.
 *
 * <h3>Selecting specific tracks</h3>
 *
 * Track selection overrides can be used to select specific tracks. To specify an override for a
 * renderer, it's first necessary to obtain the tracks that have been mapped to it:
 *
 * <pre>{@code
 * MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
 * TrackGroupArray rendererTrackGroups = mappedTrackInfo == null ? null
 *     : mappedTrackInfo.getTrackGroups(rendererIndex);
 * }</pre>
 *
 * If {@code rendererTrackGroups} is null then there aren't any currently mapped tracks, and so
 * setting an override isn't possible. Note that a {@link Player.EventListener} registered on the
 * player can be used to determine when the current tracks (and therefore the mapping) changes. If
 * {@code rendererTrackGroups} is non-null then an override can be set. The next step is to query
 * the properties of the available tracks to determine the {@code groupIndex} and the {@code
 * trackIndices} within the group it that should be selected. The override can then be specified
 * using {@link ParametersBuilder#setSelectionOverride}:
 *
 * <pre>{@code
 * SelectionOverride selectionOverride = new SelectionOverride(groupIndex, trackIndices);
 * trackSelector.setParameters(
 *     trackSelector
 *         .buildUponParameters()
 *         .setSelectionOverride(rendererIndex, rendererTrackGroups, selectionOverride));
 * }</pre>
 *
 * <h3>Constraint based track selection</h3>
 *
 * Whilst track selection overrides make it possible to select specific tracks, the recommended way
 * of controlling which tracks are selected is by specifying constraints. For example consider the
 * case of wanting to restrict video track selections to SD, and preferring German audio tracks.
 * Track selection overrides could be used to select specific tracks meeting these criteria, however
 * a simpler and more flexible approach is to specify these constraints directly:
 *
 * <pre>{@code
 * trackSelector.setParameters(
 *     trackSelector
 *         .buildUponParameters()
 *         .setMaxVideoSizeSd()
 *         .setPreferredAudioLanguage("deu"));
 * }</pre>
 *
 * There are several benefits to using constraint based track selection instead of specific track
 * overrides:
 *
 * <ul>
 *   <li>You can specify constraints before knowing what tracks the media provides. This can
 *       simplify track selection code (e.g. you don't have to listen for changes in the available
 *       tracks before configuring the selector).
 *   <li>Constraints can be applied consistently across all periods in a complex piece of media,
 *       even if those periods contain different tracks. In contrast, a specific track override is
 *       only applied to periods whose tracks match those for which the override was set.
 * </ul>
 *
 * <h3>Disabling renderers</h3>
 *
 * Renderers can be disabled using {@link ParametersBuilder#setRendererDisabled}. Disabling a
 * renderer differs from setting a {@code null} override because the renderer is disabled
 * unconditionally, whereas a {@code null} override is applied only when the track groups available
 * to the renderer match the {@link TrackGroupArray} for which it was specified.
 *
 * <h3>Tunneling</h3>
 *
 * Tunneled playback can be enabled in cases where the combination of renderers and selected tracks
 * support it. Tunneled playback is enabled by passing an audio session ID to {@link
 * ParametersBuilder#setTunnelingAudioSessionId(int)}.
 */
public class DefaultTrackSelector extends MappingTrackSelector {

}
