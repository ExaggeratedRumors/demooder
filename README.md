# Demooder

![Android support](https://shields.io/badge/Android-SDK_34-green) ![Model](https://shields.io/badge/KotlinDL-0.5.0-purple) ![In progress](https://shields.io/badge/In_progress-purple)

<p align="center">
    <img src="images/logo.png" width="200" alt="logo"/> 
</p>

Android application using input sound to recognize voice.

## Release

`
in progress
`

## Technologies

- Gradle 8.2
- JVM 11
- Android SDK 34
- Kotlin 1.9.20
- Jetpack Compose 1.6.10
- Compose Multiplatform 1.6.10
- KotlinDL 0.5.2
- cudnn 7.6.3

## Modules

- `app` - mobile application.
- `model` - CNN model execution.
- `processing` - Common library.

## Executing
1. Clone repository:
```agsl
https://github.com/ExaggeratedRumors/demooder.git
```
2. Download AudioWav data: <a href="https://www.kaggle.com/api/v1/datasets/download/ejlok1/cremad">Download from Kaggle</a>.
3. Unzip Wav files in `data_audio` directory (from root it's `demooder-model/data_audio` directory).
4. [optional] Run data augmentation task:
```bash
./gradlew dataAugmentation
```
5. Run create spectrograms task:
```bash
./gradlew createSpectrograms
```
6. Run model training task:
```bash
./gradlew trainModel
```
7. Output model is saved in `data_models` directory.

## Sound data

Source: <a href="https://cheyneycomputerscience.github.io/CREMA-D/">CREMA-D</a>

## Audio data augmentation
1. Audio data augmentation: <a href="https://medium.com/@notabelardoriojas/environmental-sound-classification-investigating-different-spectrograms-and-audio-augmentation-95f6989d0ae5">about audio data augmentation</a>.
2. Gaussian noise.
3. Time stretching.

## Sound signal processing

1. Read WAV files according to the header scheme: <a href="http://soundfile.sapp.org/doc/WaveFormat/">wav file format</a>.
2. Convert byte data to complex.
3. Signal windowing: <a href="https://download.ni.com/evaluation/pxi/Understanding%20FFTs%20and%20Windowing.pdf">about windowing</a>.
4. Use Short-Time Fourier Transform (STFT): <a href="https://brianmcfee.net/dstbook-site/content/ch09-stft/STFT.html">about STFT</a>, <a href="https://www.ni.com/docs/en-US/bundle/diadem/page/genmaths/genmaths/calc_fouriertransform.html">about FFT</a>.
5. Filter by A-weighting or C-weighting: <a href="https://www.noisemeters.com/help/faq/frequency-weighting/">about weighting</a>.

## Predicting

1. Read classifier model.
2. Record voice signal.
3. Save as WAV file. 
4. Down-sampling signal from 48000Hz to 16000Hz: <a href="https://gist.github.com/mattmalec/6ceee1f3961ff3068727ca98ff199fab">about resampling</a>.
5. Convert byte data to complex.
6. Signal windowing and filter by weighting.
7. Predict.

## Visualizing
1. Read data.
2. Use FFT.
3. Convert FFt to spectral amplitude.
4. Convert to octave/thirds bands: <a href="https://sengpielaudio.com/calculator-octave.htm">about octave to third conversion</a>.
5. Filter by A-weighting or C-weighting.

## Build network

1. Build VGG architecture model: <a href="https://viso.ai/deep-learning/vgg-very-deep-convolutional-networks/">about VGG</a>.

## Additional requirements

1. CUDA for training model on GPU (Nvidia graphics cards):
2. NNAPI for mobile devices environment acceleration: <a href="https://blog.jetbrains.com/kotlin/2022/12/kotlindl-0-5-has-come-to-android">about inference on Android </a>.