
1
trainingPlaceholder*
shape: *
dtype0

7
numberOfLossesPlaceholder*
shape: *
dtype0
\
default_data_placeholderPlaceholder*&
shape:�����������*
dtype0
r
conv2d_2_conv2d_kernel
VariableV2*
shape:*
shared_name *
dtype0*
	container 
B
ConstConst*%
valueB"            *
dtype0
D
Const_1Const*%
valueB	"               *
dtype0	
g
StatelessTruncatedNormalStatelessTruncatedNormalConstConst_1*
T0*
Tseed0	*
dtype0
8
Const_2Const*
valueB 2�m�7&�?*
dtype0
=
CastCastConst_2*

SrcT0*
Truncate( *

DstT0
K
Init_conv2d_2_conv2d_kernelMulStatelessTruncatedNormalCast*
T0
�
Assign_conv2d_2_conv2d_kernelAssignconv2d_2_conv2d_kernelInit_conv2d_2_conv2d_kernel*
use_locking(*
T0*
validate_shape(
d
conv2d_2_conv2d_bias
VariableV2*
shape:*
shared_name *
dtype0*
	container 
5
Const_3Const*
valueB:*
dtype0
D
Const_4Const*%
valueB	"               *
dtype0	
e
StatelessRandomUniformStatelessRandomUniformConst_3Const_4*
T0*
Tseed0	*
dtype0
8
Const_5Const*
valueB 2>,p� �?*
dtype0
?
Cast_1CastConst_5*

SrcT0*
Truncate( *

DstT0
I
Init_conv2d_2_conv2d_biasMulStatelessRandomUniformCast_1*
T0
�
Assign_conv2d_2_conv2d_biasAssignconv2d_2_conv2d_biasInit_conv2d_2_conv2d_bias*
use_locking(*
T0*
validate_shape(
�
Conv2dConv2Ddefault_data_placeholderconv2d_2_conv2d_kernel*
	dilations
*
T0*
strides
*
data_formatNHWC*
use_cudnn_on_gpu(*
explicit_paddings
 *
paddingSAME
P
BiasAddBiasAddConv2dconv2d_2_conv2d_bias*
T0*
data_formatNHWC

ReluReluBiasAdd*
T0
.
Activation_conv2d_2IdentityRelu*
T0
D
Const_6Const*%
valueB"            *
dtype0
D
Const_7Const*%
valueB"            *
dtype0
o
MaxPool	MaxPoolV2Activation_conv2d_2Const_6Const_7*
paddingVALID*
T0*
data_formatNHWC
r
conv2d_4_conv2d_kernel
VariableV2*
shape: *
shared_name *
dtype0*
	container 
D
Const_8Const*%
valueB"             *
dtype0
D
Const_9Const*%
valueB	"               *
dtype0	
k
StatelessTruncatedNormal_1StatelessTruncatedNormalConst_8Const_9*
T0*
Tseed0	*
dtype0
9
Const_10Const*
valueB 2�m�7&�?*
dtype0
@
Cast_2CastConst_10*

SrcT0*
Truncate( *

DstT0
O
Init_conv2d_4_conv2d_kernelMulStatelessTruncatedNormal_1Cast_2*
T0
�
Assign_conv2d_4_conv2d_kernelAssignconv2d_4_conv2d_kernelInit_conv2d_4_conv2d_kernel*
use_locking(*
T0*
validate_shape(
d
conv2d_4_conv2d_bias
VariableV2*
shape: *
shared_name *
dtype0*
	container 
6
Const_11Const*
valueB: *
dtype0
E
Const_12Const*%
valueB	"               *
dtype0	
i
StatelessRandomUniform_1StatelessRandomUniformConst_11Const_12*
T0*
Tseed0	*
dtype0
9
Const_13Const*
valueB 2>,p� �?*
dtype0
@
Cast_3CastConst_13*

SrcT0*
Truncate( *

DstT0
K
Init_conv2d_4_conv2d_biasMulStatelessRandomUniform_1Cast_3*
T0
�
Assign_conv2d_4_conv2d_biasAssignconv2d_4_conv2d_biasInit_conv2d_4_conv2d_bias*
use_locking(*
T0*
validate_shape(
�
Conv2d_1Conv2DMaxPoolconv2d_4_conv2d_kernel*
	dilations
*
T0*
strides
*
data_formatNHWC*
use_cudnn_on_gpu(*
explicit_paddings
 *
paddingSAME
T
	BiasAdd_1BiasAddConv2d_1conv2d_4_conv2d_bias*
T0*
data_formatNHWC
"
Relu_1Relu	BiasAdd_1*
T0
0
Activation_conv2d_4IdentityRelu_1*
T0
E
Const_14Const*%
valueB"            *
dtype0
E
Const_15Const*%
valueB"            *
dtype0
s
	MaxPool_1	MaxPoolV2Activation_conv2d_4Const_14Const_15*
paddingVALID*
T0*
data_formatNHWC
r
conv2d_7_conv2d_kernel
VariableV2*
shape: *
shared_name *
dtype0*
	container 
E
Const_16Const*%
valueB"             *
dtype0
E
Const_17Const*%
valueB	"               *
dtype0	
m
StatelessTruncatedNormal_2StatelessTruncatedNormalConst_16Const_17*
T0*
Tseed0	*
dtype0
9
Const_18Const*
valueB 2�V��@�?*
dtype0
@
Cast_4CastConst_18*

SrcT0*
Truncate( *

DstT0
O
Init_conv2d_7_conv2d_kernelMulStatelessTruncatedNormal_2Cast_4*
T0
�
Assign_conv2d_7_conv2d_kernelAssignconv2d_7_conv2d_kernelInit_conv2d_7_conv2d_kernel*
use_locking(*
T0*
validate_shape(
d
conv2d_7_conv2d_bias
VariableV2*
shape:*
shared_name *
dtype0*
	container 
6
Const_19Const*
valueB:*
dtype0
E
Const_20Const*%
valueB	"               *
dtype0	
i
StatelessRandomUniform_2StatelessRandomUniformConst_19Const_20*
T0*
Tseed0	*
dtype0
9
Const_21Const*
valueB 23�E�y�?*
dtype0
@
Cast_5CastConst_21*

SrcT0*
Truncate( *

DstT0
K
Init_conv2d_7_conv2d_biasMulStatelessRandomUniform_2Cast_5*
T0
�
Assign_conv2d_7_conv2d_biasAssignconv2d_7_conv2d_biasInit_conv2d_7_conv2d_bias*
use_locking(*
T0*
validate_shape(
�
Conv2d_2Conv2D	MaxPool_1conv2d_7_conv2d_kernel*
	dilations
*
T0*
strides
*
data_formatNHWC*
use_cudnn_on_gpu(*
explicit_paddings
 *
paddingSAME
T
	BiasAdd_2BiasAddConv2d_2conv2d_7_conv2d_bias*
T0*
data_formatNHWC
"
Relu_2Relu	BiasAdd_2*
T0
0
Activation_conv2d_7IdentityRelu_2*
T0
E
Const_22Const*%
valueB"            *
dtype0
E
Const_23Const*%
valueB"            *
dtype0
s
	MaxPool_2	MaxPoolV2Activation_conv2d_7Const_22Const_23*
paddingVALID*
T0*
data_formatNHWC
r
conv2d_9_conv2d_kernel
VariableV2*
shape: *
shared_name *
dtype0*
	container 
E
Const_24Const*%
valueB"             *
dtype0
E
Const_25Const*%
valueB	"               *
dtype0	
m
StatelessTruncatedNormal_3StatelessTruncatedNormalConst_24Const_25*
T0*
Tseed0	*
dtype0
9
Const_26Const*
valueB 2�m�7&�?*
dtype0
@
Cast_6CastConst_26*

SrcT0*
Truncate( *

DstT0
O
Init_conv2d_9_conv2d_kernelMulStatelessTruncatedNormal_3Cast_6*
T0
�
Assign_conv2d_9_conv2d_kernelAssignconv2d_9_conv2d_kernelInit_conv2d_9_conv2d_kernel*
use_locking(*
T0*
validate_shape(
d
conv2d_9_conv2d_bias
VariableV2*
shape: *
shared_name *
dtype0*
	container 
6
Const_27Const*
valueB: *
dtype0
E
Const_28Const*%
valueB	"               *
dtype0	
i
StatelessRandomUniform_3StatelessRandomUniformConst_27Const_28*
T0*
Tseed0	*
dtype0
9
Const_29Const*
valueB 2>,p� �?*
dtype0
@
Cast_7CastConst_29*

SrcT0*
Truncate( *

DstT0
K
Init_conv2d_9_conv2d_biasMulStatelessRandomUniform_3Cast_7*
T0
�
Assign_conv2d_9_conv2d_biasAssignconv2d_9_conv2d_biasInit_conv2d_9_conv2d_bias*
use_locking(*
T0*
validate_shape(
�
Conv2d_3Conv2D	MaxPool_2conv2d_9_conv2d_kernel*
	dilations
*
T0*
strides
*
data_formatNHWC*
use_cudnn_on_gpu(*
explicit_paddings
 *
paddingSAME
T
	BiasAdd_3BiasAddConv2d_3conv2d_9_conv2d_bias*
T0*
data_formatNHWC
"
Relu_3Relu	BiasAdd_3*
T0
0
Activation_conv2d_9IdentityRelu_3*
T0
E
Const_30Const*%
valueB"            *
dtype0
E
Const_31Const*%
valueB"            *
dtype0
s
	MaxPool_3	MaxPoolV2Activation_conv2d_9Const_30Const_31*
paddingVALID*
T0*
data_formatNHWC
=
Const_32Const*
valueB"����   *
dtype0
>
ReshapeReshape	MaxPool_3Const_32*
T0*
Tshape0
j
dense_13_dense_kernel
VariableV2*
shape:	�@*
shared_name *
dtype0*
	container 
=
Const_33Const*
valueB"   @   *
dtype0
E
Const_34Const*%
valueB	"               *
dtype0	
m
StatelessTruncatedNormal_4StatelessTruncatedNormalConst_33Const_34*
T0*
Tseed0	*
dtype0
9
Const_35Const*
valueB 2�����0�?*
dtype0
@
Cast_8CastConst_35*

SrcT0*
Truncate( *

DstT0
N
Init_dense_13_dense_kernelMulStatelessTruncatedNormal_4Cast_8*
T0
�
Assign_dense_13_dense_kernelAssigndense_13_dense_kernelInit_dense_13_dense_kernel*
use_locking(*
T0*
validate_shape(
c
dense_13_dense_bias
VariableV2*
shape:@*
shared_name *
dtype0*
	container 
6
Const_36Const*
valueB:@*
dtype0
E
Const_37Const*%
valueB	"               *
dtype0	
i
StatelessRandomUniform_4StatelessRandomUniformConst_36Const_37*
T0*
Tseed0	*
dtype0
9
Const_38Const*
valueB 2�LX�z��?*
dtype0
@
Cast_9CastConst_38*

SrcT0*
Truncate( *

DstT0
J
Init_dense_13_dense_biasMulStatelessRandomUniform_4Cast_9*
T0
�
Assign_dense_13_dense_biasAssigndense_13_dense_biasInit_dense_13_dense_bias*
use_locking(*
T0*
validate_shape(
_
MatMulMatMulReshapedense_13_dense_kernel*
transpose_b( *
T0*
transpose_a( 
0
AddAddMatMuldense_13_dense_bias*
T0

Relu_4ReluAdd*
T0
0
Activation_dense_13IdentityRelu_4*
T0
i
dense_15_dense_kernel
VariableV2*
shape
:@ *
shared_name *
dtype0*
	container 
=
Const_39Const*
valueB"@       *
dtype0
E
Const_40Const*%
valueB	"               *
dtype0	
m
StatelessTruncatedNormal_5StatelessTruncatedNormalConst_39Const_40*
T0*
Tseed0	*
dtype0
9
Const_41Const*
valueB 2��b�R��?*
dtype0
A
Cast_10CastConst_41*

SrcT0*
Truncate( *

DstT0
O
Init_dense_15_dense_kernelMulStatelessTruncatedNormal_5Cast_10*
T0
�
Assign_dense_15_dense_kernelAssigndense_15_dense_kernelInit_dense_15_dense_kernel*
use_locking(*
T0*
validate_shape(
c
dense_15_dense_bias
VariableV2*
shape: *
shared_name *
dtype0*
	container 
6
Const_42Const*
valueB: *
dtype0
E
Const_43Const*%
valueB	"               *
dtype0	
i
StatelessRandomUniform_5StatelessRandomUniformConst_42Const_43*
T0*
Tseed0	*
dtype0
9
Const_44Const*
valueB 2.!	���?*
dtype0
A
Cast_11CastConst_44*

SrcT0*
Truncate( *

DstT0
K
Init_dense_15_dense_biasMulStatelessRandomUniform_5Cast_11*
T0
�
Assign_dense_15_dense_biasAssigndense_15_dense_biasInit_dense_15_dense_bias*
use_locking(*
T0*
validate_shape(
m
MatMul_1MatMulActivation_dense_13dense_15_dense_kernel*
transpose_b( *
T0*
transpose_a( 
4
Add_1AddMatMul_1dense_15_dense_bias*
T0

Relu_5ReluAdd_1*
T0
0
Activation_dense_15IdentityRelu_5*
T0
i
dense_17_dense_kernel
VariableV2*
shape
: *
shared_name *
dtype0*
	container 
=
Const_45Const*
valueB"       *
dtype0
E
Const_46Const*%
valueB	"               *
dtype0	
i
StatelessRandomUniform_6StatelessRandomUniformConst_45Const_46*
T0*
Tseed0	*
dtype0
9
Const_47Const*
valueB 2��hWn�?*
dtype0
A
Cast_12CastConst_47*

SrcT0*
Truncate( *

DstT0
M
Init_dense_17_dense_kernelMulStatelessRandomUniform_6Cast_12*
T0
�
Assign_dense_17_dense_kernelAssigndense_17_dense_kernelInit_dense_17_dense_kernel*
use_locking(*
T0*
validate_shape(
c
dense_17_dense_bias
VariableV2*
shape:*
shared_name *
dtype0*
	container 
6
Const_48Const*
valueB:*
dtype0
E
Const_49Const*%
valueB	"               *
dtype0	
i
StatelessRandomUniform_7StatelessRandomUniformConst_48Const_49*
T0*
Tseed0	*
dtype0
9
Const_50Const*
valueB 2�LX�z��?*
dtype0
A
Cast_13CastConst_50*

SrcT0*
Truncate( *

DstT0
K
Init_dense_17_dense_biasMulStatelessRandomUniform_7Cast_13*
T0
�
Assign_dense_17_dense_biasAssigndense_17_dense_biasInit_dense_17_dense_bias*
use_locking(*
T0*
validate_shape(
m
MatMul_2MatMulActivation_dense_15dense_17_dense_kernel*
transpose_b( *
T0*
transpose_a( 
4
Add_2AddMatMul_2dense_17_dense_bias*
T0
"
SoftmaxSoftmaxAdd_2*
T0
1
Activation_dense_17IdentitySoftmax*
T0
6
PlaceholderPlaceholder*
shape:*
dtype0
Q
SquaredDifferenceSquaredDifferenceActivation_dense_17Placeholder*
T0
;
Const_51Const*
valueB :
���������*
dtype0
O
MeanMeanSquaredDifferenceConst_51*

Tidx0*
	keep_dims( *
T0
2
Const_52Const*
value	B : *
dtype0

RankRankMean*
T0
2
Const_53Const*
value	B :*
dtype0
4
RangeRangeConst_52RankConst_53*

Tidx0
C
	ReduceSumSumMeanRange*

Tidx0*
	keep_dims( *
T0
2
Const_54Const*
value	B : *
dtype0
*
Rank_1RankSquaredDifference*
T0
2
Const_55Const*
value	B :*
dtype0
8
Range_1RangeConst_54Rank_1Const_55*

Tidx0
T
ReduceSum_1SumSquaredDifferenceRange_1*

Tidx0*
	keep_dims( *
T0
:
DivNoNanDivNoNanReduceSum_1numberOfLosses*
T0
4
default_training_lossIdentityDivNoNan*
T0
>
Gradients/OnesLikeOnesLikedefault_training_loss*
T0
;
Gradients/IdentityIdentityGradients/OnesLike*
T0
K
Gradients/DivNoNanDivNoNanGradients/IdentitynumberOfLosses*
T0
-
Gradients/NegateNegReduceSum_1*
T0
K
Gradients/DivNoNan_1DivNoNanGradients/NegatenumberOfLosses*
T0
O
Gradients/DivNoNan_2DivNoNanGradients/DivNoNan_1numberOfLosses*
T0
L
Gradients/MultiplyMulGradients/IdentityGradients/DivNoNan_2*
T0
>
Gradients/ShapeShapeReduceSum_1*
T0*
out_type0
C
Gradients/Shape_1ShapenumberOfLosses*
T0*
out_type0
e
Gradients/BroadcastGradientArgsBroadcastGradientArgsGradients/ShapeGradients/Shape_1*
T0
o
Gradients/SumSumGradients/DivNoNanGradients/BroadcastGradientArgs*

Tidx0*
	keep_dims( *
T0
S
Gradients/ReshapeReshapeGradients/SumGradients/Shape*
T0*
Tshape0
s
Gradients/Sum_1SumGradients/Multiply!Gradients/BroadcastGradientArgs:1*

Tidx0*
	keep_dims( *
T0
Y
Gradients/Reshape_1ReshapeGradients/Sum_1Gradients/Shape_1*
T0*
Tshape0
F
Gradients/Shape_2ShapeSquaredDifference*
T0*
out_type0
9
Gradients/ConstConst*
value	B : *
dtype0
;
Gradients/Const_1Const*
value	B :*
dtype0
B
Gradients/SizeSizeGradients/Shape_2*
T0*
out_type0
6
Gradients/AddAddRange_1Gradients/Size*
T0
<
Gradients/ModModGradients/AddGradients/Size*
T0
X
Gradients/RangeRangeGradients/ConstGradients/SizeGradients/Const_1*

Tidx0
8
Gradients/OnesLike_1OnesLikeGradients/Mod*
T0
�
Gradients/DynamicStitchDynamicStitchGradients/RangeGradients/ModGradients/Shape_2Gradients/OnesLike_1*
T0*
N
;
Gradients/Const_2Const*
value	B :*
dtype0
Q
Gradients/MaximumMaximumGradients/DynamicStitchGradients/Const_2*
T0
C
Gradients/DivDivGradients/Shape_2Gradients/Maximum*
T0
a
Gradients/Reshape_2ReshapeGradients/ReshapeGradients/DynamicStitch*
T0*
Tshape0
U
Gradients/TileTileGradients/Reshape_2Gradients/Div*

Tmultiples0*
T0
;
Gradients/Const_3Const*
value	B :*
dtype0
Q
Gradients/CastCastGradients/Const_3*

SrcT0*
Truncate( *

DstT0
D
Gradients/SubtractSubActivation_dense_17Placeholder*
T0
H
Gradients/Multiply_1MulGradients/CastGradients/Subtract*
T0
J
Gradients/Multiply_2MulGradients/TileGradients/Multiply_1*
T0
8
Gradients/Negate_1NegGradients/Multiply_2*
T0
H
Gradients/Shape_3ShapeActivation_dense_17*
T0*
out_type0
@
Gradients/Shape_4ShapePlaceholder*
T0*
out_type0
i
!Gradients/BroadcastGradientArgs_1BroadcastGradientArgsGradients/Shape_3Gradients/Shape_4*
T0
u
Gradients/Sum_2SumGradients/Multiply_2!Gradients/BroadcastGradientArgs_1*

Tidx0*
	keep_dims( *
T0
Y
Gradients/Reshape_3ReshapeGradients/Sum_2Gradients/Shape_3*
T0*
Tshape0
u
Gradients/Sum_3SumGradients/Negate_1#Gradients/BroadcastGradientArgs_1:1*

Tidx0*
	keep_dims( *
T0
Y
Gradients/Reshape_4ReshapeGradients/Sum_3Gradients/Shape_4*
T0*
Tshape0
>
Gradients/Identity_1IdentityGradients/Reshape_3*
T0
C
Gradients/Multiply_3MulGradients/Identity_1Softmax*
T0
E
Gradients/Const_4/ConstConst*
valueB:*
dtype0
k
Gradients/Sum_4SumGradients/Multiply_3Gradients/Const_4/Const*

Tidx0*
	keep_dims( *
T0
L
Gradients/Const_5/ConstConst*
valueB"����   *
dtype0
_
Gradients/Reshape_5ReshapeGradients/Sum_4Gradients/Const_5/Const*
T0*
Tshape0
O
Gradients/Subtract_1SubGradients/Identity_1Gradients/Reshape_5*
T0
C
Gradients/Multiply_4MulGradients/Subtract_1Softmax*
T0
?
Gradients/Identity_2IdentityGradients/Multiply_4*
T0
?
Gradients/Identity_3IdentityGradients/Multiply_4*
T0
=
Gradients/Shape_5ShapeMatMul_2*
T0*
out_type0
H
Gradients/Shape_6Shapedense_17_dense_bias*
T0*
out_type0
i
!Gradients/BroadcastGradientArgs_2BroadcastGradientArgsGradients/Shape_5Gradients/Shape_6*
T0
u
Gradients/Sum_5SumGradients/Identity_2!Gradients/BroadcastGradientArgs_2*

Tidx0*
	keep_dims( *
T0
Y
Gradients/Reshape_6ReshapeGradients/Sum_5Gradients/Shape_5*
T0*
Tshape0
w
Gradients/Sum_6SumGradients/Identity_3#Gradients/BroadcastGradientArgs_2:1*

Tidx0*
	keep_dims( *
T0
Y
Gradients/Reshape_7ReshapeGradients/Sum_6Gradients/Shape_6*
T0*
Tshape0
u
Gradients/MatMulMatMulGradients/Reshape_6dense_17_dense_kernel*
transpose_b(*
T0*
transpose_a( 
u
Gradients/MatMul_1MatMulActivation_dense_15Gradients/Reshape_6*
transpose_b( *
T0*
transpose_a(
;
Gradients/Identity_4IdentityGradients/MatMul*
T0
D
Gradients/ReluGradReluGradGradients/Identity_4Add_1*
T0
=
Gradients/Identity_5IdentityGradients/ReluGrad*
T0
=
Gradients/Identity_6IdentityGradients/ReluGrad*
T0
=
Gradients/Shape_7ShapeMatMul_1*
T0*
out_type0
H
Gradients/Shape_8Shapedense_15_dense_bias*
T0*
out_type0
i
!Gradients/BroadcastGradientArgs_3BroadcastGradientArgsGradients/Shape_7Gradients/Shape_8*
T0
u
Gradients/Sum_7SumGradients/Identity_5!Gradients/BroadcastGradientArgs_3*

Tidx0*
	keep_dims( *
T0
Y
Gradients/Reshape_8ReshapeGradients/Sum_7Gradients/Shape_7*
T0*
Tshape0
w
Gradients/Sum_8SumGradients/Identity_6#Gradients/BroadcastGradientArgs_3:1*

Tidx0*
	keep_dims( *
T0
Y
Gradients/Reshape_9ReshapeGradients/Sum_8Gradients/Shape_8*
T0*
Tshape0
w
Gradients/MatMul_2MatMulGradients/Reshape_8dense_15_dense_kernel*
transpose_b(*
T0*
transpose_a( 
u
Gradients/MatMul_3MatMulActivation_dense_13Gradients/Reshape_8*
transpose_b( *
T0*
transpose_a(
=
Gradients/Identity_7IdentityGradients/MatMul_2*
T0
D
Gradients/ReluGrad_1ReluGradGradients/Identity_7Add*
T0
?
Gradients/Identity_8IdentityGradients/ReluGrad_1*
T0
?
Gradients/Identity_9IdentityGradients/ReluGrad_1*
T0
;
Gradients/Shape_9ShapeMatMul*
T0*
out_type0
I
Gradients/Shape_10Shapedense_13_dense_bias*
T0*
out_type0
j
!Gradients/BroadcastGradientArgs_4BroadcastGradientArgsGradients/Shape_9Gradients/Shape_10*
T0
u
Gradients/Sum_9SumGradients/Identity_8!Gradients/BroadcastGradientArgs_4*

Tidx0*
	keep_dims( *
T0
Z
Gradients/Reshape_10ReshapeGradients/Sum_9Gradients/Shape_9*
T0*
Tshape0
x
Gradients/Sum_10SumGradients/Identity_9#Gradients/BroadcastGradientArgs_4:1*

Tidx0*
	keep_dims( *
T0
\
Gradients/Reshape_11ReshapeGradients/Sum_10Gradients/Shape_10*
T0*
Tshape0
x
Gradients/MatMul_4MatMulGradients/Reshape_10dense_13_dense_kernel*
transpose_b(*
T0*
transpose_a( 
j
Gradients/MatMul_5MatMulReshapeGradients/Reshape_10*
transpose_b( *
T0*
transpose_a(
?
Gradients/Shape_11Shape	MaxPool_3*
T0*
out_type0
^
Gradients/Reshape_12ReshapeGradients/MatMul_4Gradients/Shape_11*
T0*
Tshape0
�
Gradients/MaxPoolGradV2MaxPoolGradV2Activation_conv2d_9	MaxPool_3Gradients/Reshape_12Const_30Const_31*
paddingVALID*
T0*
data_formatNHWC
C
Gradients/Identity_10IdentityGradients/MaxPoolGradV2*
T0
K
Gradients/ReluGrad_2ReluGradGradients/Identity_10	BiasAdd_3*
T0
Z
Gradients/BiasAddGradBiasAddGradGradients/ReluGrad_2*
T0*
data_formatNHWC
@
Gradients/Identity_11IdentityGradients/ReluGrad_2*
T0
?
Gradients/Shape_12Shape	MaxPool_2*
T0*
out_type0
�
Gradients/Conv2DBackpropInputConv2DBackpropInputGradients/Shape_12conv2d_9_conv2d_kernelGradients/Identity_11*
	dilations
*
T0*
data_formatNHWC*
strides
*
explicit_paddings
 *
use_cudnn_on_gpu(*
paddingSAME
L
Gradients/Shape_13Shapeconv2d_9_conv2d_kernel*
T0*
out_type0
�
Gradients/Conv2DBackpropFilterConv2DBackpropFilter	MaxPool_2Gradients/Shape_13Gradients/Identity_11*
	dilations
*
T0*
data_formatNHWC*
strides
*
explicit_paddings
 *
use_cudnn_on_gpu(*
paddingSAME
�
Gradients/MaxPoolGradV2_1MaxPoolGradV2Activation_conv2d_7	MaxPool_2Gradients/Conv2DBackpropInputConst_22Const_23*
paddingVALID*
T0*
data_formatNHWC
E
Gradients/Identity_12IdentityGradients/MaxPoolGradV2_1*
T0
K
Gradients/ReluGrad_3ReluGradGradients/Identity_12	BiasAdd_2*
T0
\
Gradients/BiasAddGrad_1BiasAddGradGradients/ReluGrad_3*
T0*
data_formatNHWC
@
Gradients/Identity_13IdentityGradients/ReluGrad_3*
T0
?
Gradients/Shape_14Shape	MaxPool_1*
T0*
out_type0
�
Gradients/Conv2DBackpropInput_1Conv2DBackpropInputGradients/Shape_14conv2d_7_conv2d_kernelGradients/Identity_13*
	dilations
*
T0*
data_formatNHWC*
strides
*
explicit_paddings
 *
use_cudnn_on_gpu(*
paddingSAME
L
Gradients/Shape_15Shapeconv2d_7_conv2d_kernel*
T0*
out_type0
�
 Gradients/Conv2DBackpropFilter_1Conv2DBackpropFilter	MaxPool_1Gradients/Shape_15Gradients/Identity_13*
	dilations
*
T0*
data_formatNHWC*
strides
*
explicit_paddings
 *
use_cudnn_on_gpu(*
paddingSAME
�
Gradients/MaxPoolGradV2_2MaxPoolGradV2Activation_conv2d_4	MaxPool_1Gradients/Conv2DBackpropInput_1Const_14Const_15*
paddingVALID*
T0*
data_formatNHWC
E
Gradients/Identity_14IdentityGradients/MaxPoolGradV2_2*
T0
K
Gradients/ReluGrad_4ReluGradGradients/Identity_14	BiasAdd_1*
T0
\
Gradients/BiasAddGrad_2BiasAddGradGradients/ReluGrad_4*
T0*
data_formatNHWC
@
Gradients/Identity_15IdentityGradients/ReluGrad_4*
T0
=
Gradients/Shape_16ShapeMaxPool*
T0*
out_type0
�
Gradients/Conv2DBackpropInput_2Conv2DBackpropInputGradients/Shape_16conv2d_4_conv2d_kernelGradients/Identity_15*
	dilations
*
T0*
data_formatNHWC*
strides
*
explicit_paddings
 *
use_cudnn_on_gpu(*
paddingSAME
L
Gradients/Shape_17Shapeconv2d_4_conv2d_kernel*
T0*
out_type0
�
 Gradients/Conv2DBackpropFilter_2Conv2DBackpropFilterMaxPoolGradients/Shape_17Gradients/Identity_15*
	dilations
*
T0*
data_formatNHWC*
strides
*
explicit_paddings
 *
use_cudnn_on_gpu(*
paddingSAME
�
Gradients/MaxPoolGradV2_3MaxPoolGradV2Activation_conv2d_2MaxPoolGradients/Conv2DBackpropInput_2Const_6Const_7*
paddingVALID*
T0*
data_formatNHWC
E
Gradients/Identity_16IdentityGradients/MaxPoolGradV2_3*
T0
I
Gradients/ReluGrad_5ReluGradGradients/Identity_16BiasAdd*
T0
\
Gradients/BiasAddGrad_3BiasAddGradGradients/ReluGrad_5*
T0*
data_formatNHWC
@
Gradients/Identity_17IdentityGradients/ReluGrad_5*
T0
N
Gradients/Shape_18Shapedefault_data_placeholder*
T0*
out_type0
�
Gradients/Conv2DBackpropInput_3Conv2DBackpropInputGradients/Shape_18conv2d_2_conv2d_kernelGradients/Identity_17*
	dilations
*
T0*
data_formatNHWC*
strides
*
explicit_paddings
 *
use_cudnn_on_gpu(*
paddingSAME
L
Gradients/Shape_19Shapeconv2d_2_conv2d_kernel*
T0*
out_type0
�
 Gradients/Conv2DBackpropFilter_3Conv2DBackpropFilterdefault_data_placeholderGradients/Shape_19Gradients/Identity_17*
	dilations
*
T0*
data_formatNHWC*
strides
*
explicit_paddings
 *
use_cudnn_on_gpu(*
paddingSAME
?
ShapeShapeconv2d_2_conv2d_kernel*
T0*
out_type0
5
Const_56Const*
valueB
 *    *
dtype0
[
'Init_optimizer_conv2d_2_conv2d_kernel-mFillShapeConst_56*
T0*

index_type0
~
"optimizer_conv2d_2_conv2d_kernel-m
VariableV2*
shape:*
shared_name *
dtype0*
	container 
�
)Assign_optimizer_conv2d_2_conv2d_kernel-mAssign"optimizer_conv2d_2_conv2d_kernel-m'Init_optimizer_conv2d_2_conv2d_kernel-m*
use_locking(*
T0*
validate_shape(
A
Shape_1Shapeconv2d_2_conv2d_kernel*
T0*
out_type0
5
Const_57Const*
valueB
 *    *
dtype0
]
'Init_optimizer_conv2d_2_conv2d_kernel-vFillShape_1Const_57*
T0*

index_type0
~
"optimizer_conv2d_2_conv2d_kernel-v
VariableV2*
shape:*
shared_name *
dtype0*
	container 
�
)Assign_optimizer_conv2d_2_conv2d_kernel-vAssign"optimizer_conv2d_2_conv2d_kernel-v'Init_optimizer_conv2d_2_conv2d_kernel-v*
use_locking(*
T0*
validate_shape(
?
Shape_2Shapeconv2d_2_conv2d_bias*
T0*
out_type0
5
Const_58Const*
valueB
 *    *
dtype0
[
%Init_optimizer_conv2d_2_conv2d_bias-mFillShape_2Const_58*
T0*

index_type0
p
 optimizer_conv2d_2_conv2d_bias-m
VariableV2*
shape:*
shared_name *
dtype0*
	container 
�
'Assign_optimizer_conv2d_2_conv2d_bias-mAssign optimizer_conv2d_2_conv2d_bias-m%Init_optimizer_conv2d_2_conv2d_bias-m*
use_locking(*
T0*
validate_shape(
?
Shape_3Shapeconv2d_2_conv2d_bias*
T0*
out_type0
5
Const_59Const*
valueB
 *    *
dtype0
[
%Init_optimizer_conv2d_2_conv2d_bias-vFillShape_3Const_59*
T0*

index_type0
p
 optimizer_conv2d_2_conv2d_bias-v
VariableV2*
shape:*
shared_name *
dtype0*
	container 
�
'Assign_optimizer_conv2d_2_conv2d_bias-vAssign optimizer_conv2d_2_conv2d_bias-v%Init_optimizer_conv2d_2_conv2d_bias-v*
use_locking(*
T0*
validate_shape(
A
Shape_4Shapeconv2d_4_conv2d_kernel*
T0*
out_type0
5
Const_60Const*
valueB
 *    *
dtype0
]
'Init_optimizer_conv2d_4_conv2d_kernel-mFillShape_4Const_60*
T0*

index_type0
~
"optimizer_conv2d_4_conv2d_kernel-m
VariableV2*
shape: *
shared_name *
dtype0*
	container 
�
)Assign_optimizer_conv2d_4_conv2d_kernel-mAssign"optimizer_conv2d_4_conv2d_kernel-m'Init_optimizer_conv2d_4_conv2d_kernel-m*
use_locking(*
T0*
validate_shape(
A
Shape_5Shapeconv2d_4_conv2d_kernel*
T0*
out_type0
5
Const_61Const*
valueB
 *    *
dtype0
]
'Init_optimizer_conv2d_4_conv2d_kernel-vFillShape_5Const_61*
T0*

index_type0
~
"optimizer_conv2d_4_conv2d_kernel-v
VariableV2*
shape: *
shared_name *
dtype0*
	container 
�
)Assign_optimizer_conv2d_4_conv2d_kernel-vAssign"optimizer_conv2d_4_conv2d_kernel-v'Init_optimizer_conv2d_4_conv2d_kernel-v*
use_locking(*
T0*
validate_shape(
?
Shape_6Shapeconv2d_4_conv2d_bias*
T0*
out_type0
5
Const_62Const*
valueB
 *    *
dtype0
[
%Init_optimizer_conv2d_4_conv2d_bias-mFillShape_6Const_62*
T0*

index_type0
p
 optimizer_conv2d_4_conv2d_bias-m
VariableV2*
shape: *
shared_name *
dtype0*
	container 
�
'Assign_optimizer_conv2d_4_conv2d_bias-mAssign optimizer_conv2d_4_conv2d_bias-m%Init_optimizer_conv2d_4_conv2d_bias-m*
use_locking(*
T0*
validate_shape(
?
Shape_7Shapeconv2d_4_conv2d_bias*
T0*
out_type0
5
Const_63Const*
valueB
 *    *
dtype0
[
%Init_optimizer_conv2d_4_conv2d_bias-vFillShape_7Const_63*
T0*

index_type0
p
 optimizer_conv2d_4_conv2d_bias-v
VariableV2*
shape: *
shared_name *
dtype0*
	container 
�
'Assign_optimizer_conv2d_4_conv2d_bias-vAssign optimizer_conv2d_4_conv2d_bias-v%Init_optimizer_conv2d_4_conv2d_bias-v*
use_locking(*
T0*
validate_shape(
A
Shape_8Shapeconv2d_7_conv2d_kernel*
T0*
out_type0
5
Const_64Const*
valueB
 *    *
dtype0
]
'Init_optimizer_conv2d_7_conv2d_kernel-mFillShape_8Const_64*
T0*

index_type0
~
"optimizer_conv2d_7_conv2d_kernel-m
VariableV2*
shape: *
shared_name *
dtype0*
	container 
�
)Assign_optimizer_conv2d_7_conv2d_kernel-mAssign"optimizer_conv2d_7_conv2d_kernel-m'Init_optimizer_conv2d_7_conv2d_kernel-m*
use_locking(*
T0*
validate_shape(
A
Shape_9Shapeconv2d_7_conv2d_kernel*
T0*
out_type0
5
Const_65Const*
valueB
 *    *
dtype0
]
'Init_optimizer_conv2d_7_conv2d_kernel-vFillShape_9Const_65*
T0*

index_type0
~
"optimizer_conv2d_7_conv2d_kernel-v
VariableV2*
shape: *
shared_name *
dtype0*
	container 
�
)Assign_optimizer_conv2d_7_conv2d_kernel-vAssign"optimizer_conv2d_7_conv2d_kernel-v'Init_optimizer_conv2d_7_conv2d_kernel-v*
use_locking(*
T0*
validate_shape(
@
Shape_10Shapeconv2d_7_conv2d_bias*
T0*
out_type0
5
Const_66Const*
valueB
 *    *
dtype0
\
%Init_optimizer_conv2d_7_conv2d_bias-mFillShape_10Const_66*
T0*

index_type0
p
 optimizer_conv2d_7_conv2d_bias-m
VariableV2*
shape:*
shared_name *
dtype0*
	container 
�
'Assign_optimizer_conv2d_7_conv2d_bias-mAssign optimizer_conv2d_7_conv2d_bias-m%Init_optimizer_conv2d_7_conv2d_bias-m*
use_locking(*
T0*
validate_shape(
@
Shape_11Shapeconv2d_7_conv2d_bias*
T0*
out_type0
5
Const_67Const*
valueB
 *    *
dtype0
\
%Init_optimizer_conv2d_7_conv2d_bias-vFillShape_11Const_67*
T0*

index_type0
p
 optimizer_conv2d_7_conv2d_bias-v
VariableV2*
shape:*
shared_name *
dtype0*
	container 
�
'Assign_optimizer_conv2d_7_conv2d_bias-vAssign optimizer_conv2d_7_conv2d_bias-v%Init_optimizer_conv2d_7_conv2d_bias-v*
use_locking(*
T0*
validate_shape(
B
Shape_12Shapeconv2d_9_conv2d_kernel*
T0*
out_type0
5
Const_68Const*
valueB
 *    *
dtype0
^
'Init_optimizer_conv2d_9_conv2d_kernel-mFillShape_12Const_68*
T0*

index_type0
~
"optimizer_conv2d_9_conv2d_kernel-m
VariableV2*
shape: *
shared_name *
dtype0*
	container 
�
)Assign_optimizer_conv2d_9_conv2d_kernel-mAssign"optimizer_conv2d_9_conv2d_kernel-m'Init_optimizer_conv2d_9_conv2d_kernel-m*
use_locking(*
T0*
validate_shape(
B
Shape_13Shapeconv2d_9_conv2d_kernel*
T0*
out_type0
5
Const_69Const*
valueB
 *    *
dtype0
^
'Init_optimizer_conv2d_9_conv2d_kernel-vFillShape_13Const_69*
T0*

index_type0
~
"optimizer_conv2d_9_conv2d_kernel-v
VariableV2*
shape: *
shared_name *
dtype0*
	container 
�
)Assign_optimizer_conv2d_9_conv2d_kernel-vAssign"optimizer_conv2d_9_conv2d_kernel-v'Init_optimizer_conv2d_9_conv2d_kernel-v*
use_locking(*
T0*
validate_shape(
@
Shape_14Shapeconv2d_9_conv2d_bias*
T0*
out_type0
5
Const_70Const*
valueB
 *    *
dtype0
\
%Init_optimizer_conv2d_9_conv2d_bias-mFillShape_14Const_70*
T0*

index_type0
p
 optimizer_conv2d_9_conv2d_bias-m
VariableV2*
shape: *
shared_name *
dtype0*
	container 
�
'Assign_optimizer_conv2d_9_conv2d_bias-mAssign optimizer_conv2d_9_conv2d_bias-m%Init_optimizer_conv2d_9_conv2d_bias-m*
use_locking(*
T0*
validate_shape(
@
Shape_15Shapeconv2d_9_conv2d_bias*
T0*
out_type0
5
Const_71Const*
valueB
 *    *
dtype0
\
%Init_optimizer_conv2d_9_conv2d_bias-vFillShape_15Const_71*
T0*

index_type0
p
 optimizer_conv2d_9_conv2d_bias-v
VariableV2*
shape: *
shared_name *
dtype0*
	container 
�
'Assign_optimizer_conv2d_9_conv2d_bias-vAssign optimizer_conv2d_9_conv2d_bias-v%Init_optimizer_conv2d_9_conv2d_bias-v*
use_locking(*
T0*
validate_shape(
A
Shape_16Shapedense_13_dense_kernel*
T0*
out_type0
5
Const_72Const*
valueB
 *    *
dtype0
]
&Init_optimizer_dense_13_dense_kernel-mFillShape_16Const_72*
T0*

index_type0
v
!optimizer_dense_13_dense_kernel-m
VariableV2*
shape:	�@*
shared_name *
dtype0*
	container 
�
(Assign_optimizer_dense_13_dense_kernel-mAssign!optimizer_dense_13_dense_kernel-m&Init_optimizer_dense_13_dense_kernel-m*
use_locking(*
T0*
validate_shape(
A
Shape_17Shapedense_13_dense_kernel*
T0*
out_type0
5
Const_73Const*
valueB
 *    *
dtype0
]
&Init_optimizer_dense_13_dense_kernel-vFillShape_17Const_73*
T0*

index_type0
v
!optimizer_dense_13_dense_kernel-v
VariableV2*
shape:	�@*
shared_name *
dtype0*
	container 
�
(Assign_optimizer_dense_13_dense_kernel-vAssign!optimizer_dense_13_dense_kernel-v&Init_optimizer_dense_13_dense_kernel-v*
use_locking(*
T0*
validate_shape(
?
Shape_18Shapedense_13_dense_bias*
T0*
out_type0
5
Const_74Const*
valueB
 *    *
dtype0
[
$Init_optimizer_dense_13_dense_bias-mFillShape_18Const_74*
T0*

index_type0
o
optimizer_dense_13_dense_bias-m
VariableV2*
shape:@*
shared_name *
dtype0*
	container 
�
&Assign_optimizer_dense_13_dense_bias-mAssignoptimizer_dense_13_dense_bias-m$Init_optimizer_dense_13_dense_bias-m*
use_locking(*
T0*
validate_shape(
?
Shape_19Shapedense_13_dense_bias*
T0*
out_type0
5
Const_75Const*
valueB
 *    *
dtype0
[
$Init_optimizer_dense_13_dense_bias-vFillShape_19Const_75*
T0*

index_type0
o
optimizer_dense_13_dense_bias-v
VariableV2*
shape:@*
shared_name *
dtype0*
	container 
�
&Assign_optimizer_dense_13_dense_bias-vAssignoptimizer_dense_13_dense_bias-v$Init_optimizer_dense_13_dense_bias-v*
use_locking(*
T0*
validate_shape(
A
Shape_20Shapedense_15_dense_kernel*
T0*
out_type0
5
Const_76Const*
valueB
 *    *
dtype0
]
&Init_optimizer_dense_15_dense_kernel-mFillShape_20Const_76*
T0*

index_type0
u
!optimizer_dense_15_dense_kernel-m
VariableV2*
shape
:@ *
shared_name *
dtype0*
	container 
�
(Assign_optimizer_dense_15_dense_kernel-mAssign!optimizer_dense_15_dense_kernel-m&Init_optimizer_dense_15_dense_kernel-m*
use_locking(*
T0*
validate_shape(
A
Shape_21Shapedense_15_dense_kernel*
T0*
out_type0
5
Const_77Const*
valueB
 *    *
dtype0
]
&Init_optimizer_dense_15_dense_kernel-vFillShape_21Const_77*
T0*

index_type0
u
!optimizer_dense_15_dense_kernel-v
VariableV2*
shape
:@ *
shared_name *
dtype0*
	container 
�
(Assign_optimizer_dense_15_dense_kernel-vAssign!optimizer_dense_15_dense_kernel-v&Init_optimizer_dense_15_dense_kernel-v*
use_locking(*
T0*
validate_shape(
?
Shape_22Shapedense_15_dense_bias*
T0*
out_type0
5
Const_78Const*
valueB
 *    *
dtype0
[
$Init_optimizer_dense_15_dense_bias-mFillShape_22Const_78*
T0*

index_type0
o
optimizer_dense_15_dense_bias-m
VariableV2*
shape: *
shared_name *
dtype0*
	container 
�
&Assign_optimizer_dense_15_dense_bias-mAssignoptimizer_dense_15_dense_bias-m$Init_optimizer_dense_15_dense_bias-m*
use_locking(*
T0*
validate_shape(
?
Shape_23Shapedense_15_dense_bias*
T0*
out_type0
5
Const_79Const*
valueB
 *    *
dtype0
[
$Init_optimizer_dense_15_dense_bias-vFillShape_23Const_79*
T0*

index_type0
o
optimizer_dense_15_dense_bias-v
VariableV2*
shape: *
shared_name *
dtype0*
	container 
�
&Assign_optimizer_dense_15_dense_bias-vAssignoptimizer_dense_15_dense_bias-v$Init_optimizer_dense_15_dense_bias-v*
use_locking(*
T0*
validate_shape(
A
Shape_24Shapedense_17_dense_kernel*
T0*
out_type0
5
Const_80Const*
valueB
 *    *
dtype0
]
&Init_optimizer_dense_17_dense_kernel-mFillShape_24Const_80*
T0*

index_type0
u
!optimizer_dense_17_dense_kernel-m
VariableV2*
shape
: *
shared_name *
dtype0*
	container 
�
(Assign_optimizer_dense_17_dense_kernel-mAssign!optimizer_dense_17_dense_kernel-m&Init_optimizer_dense_17_dense_kernel-m*
use_locking(*
T0*
validate_shape(
A
Shape_25Shapedense_17_dense_kernel*
T0*
out_type0
5
Const_81Const*
valueB
 *    *
dtype0
]
&Init_optimizer_dense_17_dense_kernel-vFillShape_25Const_81*
T0*

index_type0
u
!optimizer_dense_17_dense_kernel-v
VariableV2*
shape
: *
shared_name *
dtype0*
	container 
�
(Assign_optimizer_dense_17_dense_kernel-vAssign!optimizer_dense_17_dense_kernel-v&Init_optimizer_dense_17_dense_kernel-v*
use_locking(*
T0*
validate_shape(
?
Shape_26Shapedense_17_dense_bias*
T0*
out_type0
5
Const_82Const*
valueB
 *    *
dtype0
[
$Init_optimizer_dense_17_dense_bias-mFillShape_26Const_82*
T0*

index_type0
o
optimizer_dense_17_dense_bias-m
VariableV2*
shape:*
shared_name *
dtype0*
	container 
�
&Assign_optimizer_dense_17_dense_bias-mAssignoptimizer_dense_17_dense_bias-m$Init_optimizer_dense_17_dense_bias-m*
use_locking(*
T0*
validate_shape(
?
Shape_27Shapedense_17_dense_bias*
T0*
out_type0
5
Const_83Const*
valueB
 *    *
dtype0
[
$Init_optimizer_dense_17_dense_bias-vFillShape_27Const_83*
T0*

index_type0
o
optimizer_dense_17_dense_bias-v
VariableV2*
shape:*
shared_name *
dtype0*
	container 
�
&Assign_optimizer_dense_17_dense_bias-vAssignoptimizer_dense_17_dense_bias-v$Init_optimizer_dense_17_dense_bias-v*
use_locking(*
T0*
validate_shape(
a
optimizer_beta1_power
VariableV2*
shape: *
shared_name *
dtype0*
	container 
G
Init_optimizer_beta1_powerConst*
valueB
 *fff?*
dtype0
�
Assign_optimizer_beta1_powerAssignoptimizer_beta1_powerInit_optimizer_beta1_power*
use_locking(*
T0*
validate_shape(
a
optimizer_beta2_power
VariableV2*
shape: *
shared_name *
dtype0*
	container 
G
Init_optimizer_beta2_powerConst*
valueB
 *w�?*
dtype0
�
Assign_optimizer_beta2_powerAssignoptimizer_beta2_powerInit_optimizer_beta2_power*
use_locking(*
T0*
validate_shape(
5
Const_84Const*
valueB
 *fff?*
dtype0
5
Const_85Const*
valueB
 *w�?*
dtype0
5
Const_86Const*
valueB
 *o�:*
dtype0
5
Const_87Const*
valueB
 *���3*
dtype0
5
Const_88Const*
valueB
 *��̽*
dtype0
5
Const_89Const*
valueB
 *���=*
dtype0
Y
ClipByValueClipByValue Gradients/Conv2DBackpropFilter_3Const_88Const_89*
T0
�
	ApplyAdam	ApplyAdamconv2d_2_conv2d_kernel"optimizer_conv2d_2_conv2d_kernel-m"optimizer_conv2d_2_conv2d_kernel-voptimizer_beta1_poweroptimizer_beta2_powerConst_86Const_84Const_85Const_87ClipByValue*
use_locking(*
T0*
use_nesterov( 
5
Const_90Const*
valueB
 *��̽*
dtype0
5
Const_91Const*
valueB
 *���=*
dtype0
R
ClipByValue_1ClipByValueGradients/BiasAddGrad_3Const_90Const_91*
T0
�
ApplyAdam_1	ApplyAdamconv2d_2_conv2d_bias optimizer_conv2d_2_conv2d_bias-m optimizer_conv2d_2_conv2d_bias-voptimizer_beta1_poweroptimizer_beta2_powerConst_86Const_84Const_85Const_87ClipByValue_1*
use_locking(*
T0*
use_nesterov( 
5
Const_92Const*
valueB
 *��̽*
dtype0
5
Const_93Const*
valueB
 *���=*
dtype0
[
ClipByValue_2ClipByValue Gradients/Conv2DBackpropFilter_2Const_92Const_93*
T0
�
ApplyAdam_2	ApplyAdamconv2d_4_conv2d_kernel"optimizer_conv2d_4_conv2d_kernel-m"optimizer_conv2d_4_conv2d_kernel-voptimizer_beta1_poweroptimizer_beta2_powerConst_86Const_84Const_85Const_87ClipByValue_2*
use_locking(*
T0*
use_nesterov( 
5
Const_94Const*
valueB
 *��̽*
dtype0
5
Const_95Const*
valueB
 *���=*
dtype0
R
ClipByValue_3ClipByValueGradients/BiasAddGrad_2Const_94Const_95*
T0
�
ApplyAdam_3	ApplyAdamconv2d_4_conv2d_bias optimizer_conv2d_4_conv2d_bias-m optimizer_conv2d_4_conv2d_bias-voptimizer_beta1_poweroptimizer_beta2_powerConst_86Const_84Const_85Const_87ClipByValue_3*
use_locking(*
T0*
use_nesterov( 
5
Const_96Const*
valueB
 *��̽*
dtype0
5
Const_97Const*
valueB
 *���=*
dtype0
[
ClipByValue_4ClipByValue Gradients/Conv2DBackpropFilter_1Const_96Const_97*
T0
�
ApplyAdam_4	ApplyAdamconv2d_7_conv2d_kernel"optimizer_conv2d_7_conv2d_kernel-m"optimizer_conv2d_7_conv2d_kernel-voptimizer_beta1_poweroptimizer_beta2_powerConst_86Const_84Const_85Const_87ClipByValue_4*
use_locking(*
T0*
use_nesterov( 
5
Const_98Const*
valueB
 *��̽*
dtype0
5
Const_99Const*
valueB
 *���=*
dtype0
R
ClipByValue_5ClipByValueGradients/BiasAddGrad_1Const_98Const_99*
T0
�
ApplyAdam_5	ApplyAdamconv2d_7_conv2d_bias optimizer_conv2d_7_conv2d_bias-m optimizer_conv2d_7_conv2d_bias-voptimizer_beta1_poweroptimizer_beta2_powerConst_86Const_84Const_85Const_87ClipByValue_5*
use_locking(*
T0*
use_nesterov( 
6
	Const_100Const*
valueB
 *��̽*
dtype0
6
	Const_101Const*
valueB
 *���=*
dtype0
[
ClipByValue_6ClipByValueGradients/Conv2DBackpropFilter	Const_100	Const_101*
T0
�
ApplyAdam_6	ApplyAdamconv2d_9_conv2d_kernel"optimizer_conv2d_9_conv2d_kernel-m"optimizer_conv2d_9_conv2d_kernel-voptimizer_beta1_poweroptimizer_beta2_powerConst_86Const_84Const_85Const_87ClipByValue_6*
use_locking(*
T0*
use_nesterov( 
6
	Const_102Const*
valueB
 *��̽*
dtype0
6
	Const_103Const*
valueB
 *���=*
dtype0
R
ClipByValue_7ClipByValueGradients/BiasAddGrad	Const_102	Const_103*
T0
�
ApplyAdam_7	ApplyAdamconv2d_9_conv2d_bias optimizer_conv2d_9_conv2d_bias-m optimizer_conv2d_9_conv2d_bias-voptimizer_beta1_poweroptimizer_beta2_powerConst_86Const_84Const_85Const_87ClipByValue_7*
use_locking(*
T0*
use_nesterov( 
6
	Const_104Const*
valueB
 *��̽*
dtype0
6
	Const_105Const*
valueB
 *���=*
dtype0
O
ClipByValue_8ClipByValueGradients/MatMul_5	Const_104	Const_105*
T0
�
ApplyAdam_8	ApplyAdamdense_13_dense_kernel!optimizer_dense_13_dense_kernel-m!optimizer_dense_13_dense_kernel-voptimizer_beta1_poweroptimizer_beta2_powerConst_86Const_84Const_85Const_87ClipByValue_8*
use_locking(*
T0*
use_nesterov( 
6
	Const_106Const*
valueB
 *��̽*
dtype0
6
	Const_107Const*
valueB
 *���=*
dtype0
Q
ClipByValue_9ClipByValueGradients/Reshape_11	Const_106	Const_107*
T0
�
ApplyAdam_9	ApplyAdamdense_13_dense_biasoptimizer_dense_13_dense_bias-moptimizer_dense_13_dense_bias-voptimizer_beta1_poweroptimizer_beta2_powerConst_86Const_84Const_85Const_87ClipByValue_9*
use_locking(*
T0*
use_nesterov( 
6
	Const_108Const*
valueB
 *��̽*
dtype0
6
	Const_109Const*
valueB
 *���=*
dtype0
P
ClipByValue_10ClipByValueGradients/MatMul_3	Const_108	Const_109*
T0
�
ApplyAdam_10	ApplyAdamdense_15_dense_kernel!optimizer_dense_15_dense_kernel-m!optimizer_dense_15_dense_kernel-voptimizer_beta1_poweroptimizer_beta2_powerConst_86Const_84Const_85Const_87ClipByValue_10*
use_locking(*
T0*
use_nesterov( 
6
	Const_110Const*
valueB
 *��̽*
dtype0
6
	Const_111Const*
valueB
 *���=*
dtype0
Q
ClipByValue_11ClipByValueGradients/Reshape_9	Const_110	Const_111*
T0
�
ApplyAdam_11	ApplyAdamdense_15_dense_biasoptimizer_dense_15_dense_bias-moptimizer_dense_15_dense_bias-voptimizer_beta1_poweroptimizer_beta2_powerConst_86Const_84Const_85Const_87ClipByValue_11*
use_locking(*
T0*
use_nesterov( 
6
	Const_112Const*
valueB
 *��̽*
dtype0
6
	Const_113Const*
valueB
 *���=*
dtype0
P
ClipByValue_12ClipByValueGradients/MatMul_1	Const_112	Const_113*
T0
�
ApplyAdam_12	ApplyAdamdense_17_dense_kernel!optimizer_dense_17_dense_kernel-m!optimizer_dense_17_dense_kernel-voptimizer_beta1_poweroptimizer_beta2_powerConst_86Const_84Const_85Const_87ClipByValue_12*
use_locking(*
T0*
use_nesterov( 
6
	Const_114Const*
valueB
 *��̽*
dtype0
6
	Const_115Const*
valueB
 *���=*
dtype0
Q
ClipByValue_13ClipByValueGradients/Reshape_7	Const_114	Const_115*
T0
�
ApplyAdam_13	ApplyAdamdense_17_dense_biasoptimizer_dense_17_dense_bias-moptimizer_dense_17_dense_bias-voptimizer_beta1_poweroptimizer_beta2_powerConst_86Const_84Const_85Const_87ClipByValue_13*
use_locking(*
T0*
use_nesterov( 
4
MulMuloptimizer_beta1_powerConst_84*
T0
^
AssignAssignoptimizer_beta1_powerMul*
use_locking(*
T0*
validate_shape(
6
Mul_1Muloptimizer_beta2_powerConst_85*
T0
b
Assign_1Assignoptimizer_beta2_powerMul_1*
use_locking(*
T0*
validate_shape(
8
default_outputIdentityActivation_dense_17*
T0
3
	Const_116Const*
value	B :*
dtype0
S
ArgMaxArgMaxdefault_output	Const_116*

Tidx0*
T0*
output_type0	
3
	Const_117Const*
value	B :*
dtype0
R
ArgMax_1ArgMaxPlaceholder	Const_117*

Tidx0*
T0*
output_type0	
I
EqualEqualArgMaxArgMax_1*
incompatible_shape_error(*
T0	
>
Cast_14CastEqual*

SrcT0
*
Truncate( *

DstT0
3
	Const_118Const*
value	B : *
dtype0
H
Mean_1MeanCast_14	Const_118*

Tidx0*
	keep_dims( *
T0 "�