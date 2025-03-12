# Demooder notebooks

Notebooks for creating and testing Keras models.

## Technologies

- Python 3.8.20
- Numpy 1.23.3
- TensorFlow 2.10.0
- Keras 2.10.0
- cuda 11.2
- cuDNN 8.1.0

## Model learning environment 

Create GPU-learning environment on Windows (<a href="https://www.tensorflow.org/install/source_windows?hl=pl#gpu">versions compatibility</a>):

1. Create tensorflow-gpu enviroment:
```bash
conda install -c conda-forge cudatoolkit=11.2 cudnn=8.1.0
```
2. Install jupyter notebook and libraries:
```bash
pip install tensorflow==2.10
pip install numpy==1.23
conda install keras
conda install notebook
conda install matplotlib
```
3. Work on Jupyter Notebook:
```bash
jupyter notebook
```

## Sound data

- <a href="https://cheyneycomputerscience.github.io/CREMA-D/">CREMA-D</a>
- <a href="https://www.kaggle.com/datasets/uwrfkaggler/ravdess-emotional-speech-audio">RAVDESS</a>
- <a href="https://www.kaggle.com/datasets/barelydedicated/savee-database">VEESS</a>
- <a href="https://www.kaggle.com/datasets/ejlok1/toronto-emotional-speech-set-tess">TESS</a>


## Notebooks

- `labels_extraction.ipynb` - label spectrograms data based on data source.
- `custom_classifier.ipynb` - build and test custom network modelled on a VGG16 (<a href="https://viso.ai/deep-learning/vgg-very-deep-convolutional-networks/">about VGG</a>).
- `nasnet_classifier.ipynb` - build and test NASNetMobile model.
- `mobilenet_classifier.ipynb` - build and test MobileNetV2 model.