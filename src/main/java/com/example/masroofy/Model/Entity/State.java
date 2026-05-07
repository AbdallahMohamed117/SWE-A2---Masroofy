package com.example.masroofy.Model.Entity;

/**
 * Represents the activation state of a user or entity in the Masroofy application.
 * <p>
 * The {@code State} enum defines the possible status values for user accounts
 * and other entities that require activation state tracking.
 * </p>
 *
 * <p><b>Available States:</b></p>
 * <ul>
 *   <li>{@link #ACTIVE} - The user or entity is active and can perform operations</li>
 *   <li>{@link #INACTIVE} - The user or entity is inactive and operations are restricted</li>
 * </ul>
 *
 * <p><b>Usage in the Application:</b></p>
 * <ul>
 *   <li>Tracks whether a user has completed setup and is fully active</li>
 *   <li>Controls access to application features based on activation status</li>
 *   <li>Used by the {@code Pin} entity to determine user authentication state</li>
 * </ul>
 *
 * <p><b>Typical Flow:</b></p>
 * <ol>
 *   <li>New user creates account → State may be {@code INACTIVE} until setup completes</li>
 *   <li>User completes first-time setup → State changes to {@code ACTIVE}</li>
 *   <li>User can now access all application features</li>
 * </ol>
 *
 * @version 1.0
 * @since 1.0
 * @see com.example.masroofy.Model.Pin
 */
public enum State {

    /** Indicates that the user or entity is active and fully operational. */
    ACTIVE,

    /** Indicates that the user or entity is inactive and operations are restricted. */
    INACTIVE
}