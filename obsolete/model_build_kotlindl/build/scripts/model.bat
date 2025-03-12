@rem
@rem Copyright 2015 the original author or authors.
@rem
@rem Licensed under the Apache License, Version 2.0 (the "License");
@rem you may not use this file except in compliance with the License.
@rem You may obtain a copy of the License at
@rem
@rem      https://www.apache.org/licenses/LICENSE-2.0
@rem
@rem Unless required by applicable law or agreed to in writing, software
@rem distributed under the License is distributed on an "AS IS" BASIS,
@rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@rem See the License for the specific language governing permissions and
@rem limitations under the License.
@rem

@if "%DEBUG%"=="" @echo off
@rem ##########################################################################
@rem
@rem  model startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%"=="" set DIRNAME=.
@rem This is normally unused
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Resolve any "." and ".." in APP_HOME to make it shorter.
for %%i in ("%APP_HOME%") do set APP_HOME=%%~fi

@rem Add default JVM options here. You can also use JAVA_OPTS and MODEL_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if %ERRORLEVEL% equ 0 goto execute

echo. 1>&2
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH. 1>&2
echo. 1>&2
echo Please set the JAVA_HOME variable in your environment to match the 1>&2
echo location of your Java installation. 1>&2

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto execute

echo. 1>&2
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME% 1>&2
echo. 1>&2
echo Please set the JAVA_HOME variable in your environment to match the 1>&2
echo location of your Java installation. 1>&2

goto fail

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\model-1.0-SNAPSHOT.jar;%APP_HOME%\lib\processing-jvm.jar;%APP_HOME%\lib\visualization-jvm-0.5.2.jar;%APP_HOME%\lib\kotlin-deeplearning-tensorflow-0.5.2.jar;%APP_HOME%\lib\onnx-jvm-0.5.2.jar;%APP_HOME%\lib\dataset-jvm-0.5.2.jar;%APP_HOME%\lib\impl-jvm-0.5.2.jar;%APP_HOME%\lib\klaxon-5.5.jar;%APP_HOME%\lib\kotlin-deeplearning-api-0.5.2.jar;%APP_HOME%\lib\lets-plot-batik-2.3.0.jar;%APP_HOME%\lib\lets-plot-kotlin-api-2.0.1.jar;%APP_HOME%\lib\lets-plot-common-2.3.0.jar;%APP_HOME%\lib\kotlin-logging-jvm-2.1.21.jar;%APP_HOME%\lib\kotlin-csv-jvm-0.7.3.jar;%APP_HOME%\lib\multik-default-jvm-0.2.0.jar;%APP_HOME%\lib\kotlinx-html-jvm-0.7.3.jar;%APP_HOME%\lib\multik-openblas-jvm-0.2.0.jar;%APP_HOME%\lib\multik-kotlin-jvm-0.2.0.jar;%APP_HOME%\lib\kotlin-stdlib-jdk8-1.8.21.jar;%APP_HOME%\lib\multik-core-jvm-0.2.3.jar;%APP_HOME%\lib\jackson-databind-2.18.1.jar;%APP_HOME%\lib\jackson-annotations-2.18.1.jar;%APP_HOME%\lib\jackson-core-2.18.1.jar;%APP_HOME%\lib\jackson-module-kotlin-2.18.1.jar;%APP_HOME%\lib\kotlin-reflect-1.9.22.jar;%APP_HOME%\lib\npy-0.3.5.jar;%APP_HOME%\lib\kotlin-test-junit-1.3.50.jar;%APP_HOME%\lib\kotlin-test-1.3.50.jar;%APP_HOME%\lib\kotlin-test-common-1.3.50.jar;%APP_HOME%\lib\kotlin-test-annotations-common-1.3.50.jar;%APP_HOME%\lib\kotlin-stdlib-jdk7-1.8.21.jar;%APP_HOME%\lib\kotlin-stdlib-1.9.22.jar;%APP_HOME%\lib\tensorflow-1.15.0.jar;%APP_HOME%\lib\libtensorflow-1.15.0.jar;%APP_HOME%\lib\libtensorflow_jni_gpu-1.15.0.jar;%APP_HOME%\lib\slf4j-simple-2.0.7.jar;%APP_HOME%\lib\jhdf-0.5.7.jar;%APP_HOME%\lib\slf4j-api-2.0.7.jar;%APP_HOME%\lib\annotations-13.0.jar;%APP_HOME%\lib\libtensorflow_jni-1.15.0.jar;%APP_HOME%\lib\onnxruntime-1.14.0.jar;%APP_HOME%\lib\commons-csv-1.10.0.jar;%APP_HOME%\lib\imageio-jpeg-3.8.2.jar;%APP_HOME%\lib\batik-codec-1.14.jar;%APP_HOME%\lib\commons-lang3-3.10.jar;%APP_HOME%\lib\imageio-metadata-3.8.2.jar;%APP_HOME%\lib\imageio-core-3.8.2.jar;%APP_HOME%\lib\common-image-3.8.2.jar;%APP_HOME%\lib\common-io-3.8.2.jar;%APP_HOME%\lib\common-lang-3.8.2.jar;%APP_HOME%\lib\batik-transcoder-1.14.jar;%APP_HOME%\lib\batik-bridge-1.14.jar;%APP_HOME%\lib\batik-script-1.14.jar;%APP_HOME%\lib\batik-anim-1.14.jar;%APP_HOME%\lib\batik-gvt-1.14.jar;%APP_HOME%\lib\batik-svg-dom-1.14.jar;%APP_HOME%\lib\batik-parser-1.14.jar;%APP_HOME%\lib\batik-svggen-1.14.jar;%APP_HOME%\lib\batik-awt-util-1.14.jar;%APP_HOME%\lib\batik-dom-1.14.jar;%APP_HOME%\lib\batik-css-1.14.jar;%APP_HOME%\lib\batik-xml-1.14.jar;%APP_HOME%\lib\batik-util-1.14.jar;%APP_HOME%\lib\batik-constants-1.14.jar;%APP_HOME%\lib\batik-i18n-1.14.jar;%APP_HOME%\lib\batik-ext-1.14.jar;%APP_HOME%\lib\batik-shared-resources-1.14.jar;%APP_HOME%\lib\junit-4.12.jar;%APP_HOME%\lib\xmlgraphics-commons-2.6.jar;%APP_HOME%\lib\xml-apis-ext-1.3.04.jar;%APP_HOME%\lib\hamcrest-core-1.3.jar;%APP_HOME%\lib\commons-io-1.3.1.jar;%APP_HOME%\lib\commons-logging-1.0.4.jar;%APP_HOME%\lib\xalan-2.7.2.jar;%APP_HOME%\lib\serializer-2.7.2.jar;%APP_HOME%\lib\xml-apis-1.4.01.jar


@rem Execute model
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %MODEL_OPTS%  -classpath "%CLASSPATH%"  %*

:end
@rem End local scope for the variables with windows NT shell
if %ERRORLEVEL% equ 0 goto mainEnd

:fail
rem Set variable MODEL_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
set EXIT_CODE=%ERRORLEVEL%
if %EXIT_CODE% equ 0 set EXIT_CODE=1
if not ""=="%MODEL_EXIT_CONSOLE%" exit %EXIT_CODE%
exit /b %EXIT_CODE%

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
