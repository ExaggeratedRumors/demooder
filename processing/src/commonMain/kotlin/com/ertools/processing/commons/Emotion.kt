package com.ertools.processing.commons

/**
 * Enum class representing emotions classes for audio classification.
 * Order matches the output of the model (alphabetical order):
 * - ANG: Anger
 * - DIS: Disgust
 * - FEA: Fear
 * - HAP: Happiness
 * - NEU: Neutral
 * - SAD: Sadness
 */
enum class Emotion {
    ANG, DIS, FEA, HAP, NEU, SAD
}