
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
shape: *
shared_name *
dtype0*
	container 
B
ConstConst*%
valueB"             *
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
shape: *
shared_name *
dtype0*
	container 
5
Const_3Const*
valueB: *
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
r
conv2d_3_conv2d_kernel
VariableV2*
shape:  *
shared_name *
dtype0*
	container 
D
Const_6Const*%
valueB"              *
dtype0
D
Const_7Const*%
valueB	"               *
dtype0	
k
StatelessTruncatedNormal_1StatelessTruncatedNormalConst_6Const_7*
T0*
Tseed0	*
dtype0
8
Const_8Const*
valueB 2�V��@�?*
dtype0
?
Cast_2CastConst_8*

SrcT0*
Truncate( *

DstT0
O
Init_conv2d_3_conv2d_kernelMulStatelessTruncatedNormal_1Cast_2*
T0
�
Assign_conv2d_3_conv2d_kernelAssignconv2d_3_conv2d_kernelInit_conv2d_3_conv2d_kernel*
use_locking(*
T0*
validate_shape(
d
conv2d_3_conv2d_bias
VariableV2*
shape: *
shared_name *
dtype0*
	container 
5
Const_9Const*
valueB: *
dtype0
E
Const_10Const*%
valueB	"               *
dtype0	
h
StatelessRandomUniform_1StatelessRandomUniformConst_9Const_10*
T0*
Tseed0	*
dtype0
9
Const_11Const*
valueB 23�E�y�?*
dtype0
@
Cast_3CastConst_11*

SrcT0*
Truncate( *

DstT0
K
Init_conv2d_3_conv2d_biasMulStatelessRandomUniform_1Cast_3*
T0
�
Assign_conv2d_3_conv2d_biasAssignconv2d_3_conv2d_biasInit_conv2d_3_conv2d_bias*
use_locking(*
T0*
validate_shape(
�
Conv2d_1Conv2DActivation_conv2d_2conv2d_3_conv2d_kernel*
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
	BiasAdd_1BiasAddConv2d_1conv2d_3_conv2d_bias*
T0*
data_formatNHWC
"
Relu_1Relu	BiasAdd_1*
T0
0
Activation_conv2d_3IdentityRelu_1*
T0
E
Const_12Const*%
valueB"            *
dtype0
E
Const_13Const*%
valueB"            *
dtype0
q
MaxPool	MaxPoolV2Activation_conv2d_3Const_12Const_13*
paddingVALID*
T0*
data_formatNHWC
r
conv2d_5_conv2d_kernel
VariableV2*
shape: @*
shared_name *
dtype0*
	container 
E
Const_14Const*%
valueB"          @   *
dtype0
E
Const_15Const*%
valueB	"               *
dtype0	
m
StatelessTruncatedNormal_2StatelessTruncatedNormalConst_14Const_15*
T0*
Tseed0	*
dtype0
9
Const_16Const*
valueB 2�V��@�?*
dtype0
@
Cast_4CastConst_16*

SrcT0*
Truncate( *

DstT0
O
Init_conv2d_5_conv2d_kernelMulStatelessTruncatedNormal_2Cast_4*
T0
�
Assign_conv2d_5_conv2d_kernelAssignconv2d_5_conv2d_kernelInit_conv2d_5_conv2d_kernel*
use_locking(*
T0*
validate_shape(
d
conv2d_5_conv2d_bias
VariableV2*
shape:@*
shared_name *
dtype0*
	container 
6
Const_17Const*
valueB:@*
dtype0
E
Const_18Const*%
valueB	"               *
dtype0	
i
StatelessRandomUniform_2StatelessRandomUniformConst_17Const_18*
T0*
Tseed0	*
dtype0
9
Const_19Const*
valueB 23�E�y�?*
dtype0
@
Cast_5CastConst_19*

SrcT0*
Truncate( *

DstT0
K
Init_conv2d_5_conv2d_biasMulStatelessRandomUniform_2Cast_5*
T0
�
Assign_conv2d_5_conv2d_biasAssignconv2d_5_conv2d_biasInit_conv2d_5_conv2d_bias*
use_locking(*
T0*
validate_shape(
�
Conv2d_2Conv2DMaxPoolconv2d_5_conv2d_kernel*
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
	BiasAdd_2BiasAddConv2d_2conv2d_5_conv2d_bias*
T0*
data_formatNHWC
"
Relu_2Relu	BiasAdd_2*
T0
0
Activation_conv2d_5IdentityRelu_2*
T0
r
conv2d_6_conv2d_kernel
VariableV2*
shape:@@*
shared_name *
dtype0*
	container 
E
Const_20Const*%
valueB"      @   @   *
dtype0
E
Const_21Const*%
valueB	"               *
dtype0	
m
StatelessTruncatedNormal_3StatelessTruncatedNormalConst_20Const_21*
T0*
Tseed0	*
dtype0
9
Const_22Const*
valueB 2�m�7&�?*
dtype0
@
Cast_6CastConst_22*

SrcT0*
Truncate( *

DstT0
O
Init_conv2d_6_conv2d_kernelMulStatelessTruncatedNormal_3Cast_6*
T0
�
Assign_conv2d_6_conv2d_kernelAssignconv2d_6_conv2d_kernelInit_conv2d_6_conv2d_kernel*
use_locking(*
T0*
validate_shape(
d
conv2d_6_conv2d_bias
VariableV2*
shape:@*
shared_name *
dtype0*
	container 
6
Const_23Const*
valueB:@*
dtype0
E
Const_24Const*%
valueB	"               *
dtype0	
i
StatelessRandomUniform_3StatelessRandomUniformConst_23Const_24*
T0*
Tseed0	*
dtype0
9
Const_25Const*
valueB 2>,p� �?*
dtype0
@
Cast_7CastConst_25*

SrcT0*
Truncate( *

DstT0
K
Init_conv2d_6_conv2d_biasMulStatelessRandomUniform_3Cast_7*
T0
�
Assign_conv2d_6_conv2d_biasAssignconv2d_6_conv2d_biasInit_conv2d_6_conv2d_bias*
use_locking(*
T0*
validate_shape(
�
Conv2d_3Conv2DActivation_conv2d_5conv2d_6_conv2d_kernel*
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
	BiasAdd_3BiasAddConv2d_3conv2d_6_conv2d_bias*
T0*
data_formatNHWC
"
Relu_3Relu	BiasAdd_3*
T0
0
Activation_conv2d_6IdentityRelu_3*
T0
E
Const_26Const*%
valueB"            *
dtype0
E
Const_27Const*%
valueB"            *
dtype0
s
	MaxPool_1	MaxPoolV2Activation_conv2d_6Const_26Const_27*
paddingVALID*
T0*
data_formatNHWC
s
conv2d_8_conv2d_kernel
VariableV2*
shape:@�*
shared_name *
dtype0*
	container 
E
Const_28Const*%
valueB"      @   �   *
dtype0
E
Const_29Const*%
valueB	"               *
dtype0	
m
StatelessTruncatedNormal_4StatelessTruncatedNormalConst_28Const_29*
T0*
Tseed0	*
dtype0
9
Const_30Const*
valueB 2�m�7&�?*
dtype0
@
Cast_8CastConst_30*

SrcT0*
Truncate( *

DstT0
O
Init_conv2d_8_conv2d_kernelMulStatelessTruncatedNormal_4Cast_8*
T0
�
Assign_conv2d_8_conv2d_kernelAssignconv2d_8_conv2d_kernelInit_conv2d_8_conv2d_kernel*
use_locking(*
T0*
validate_shape(
e
conv2d_8_conv2d_bias
VariableV2*
shape:�*
shared_name *
dtype0*
	container 
7
Const_31Const*
valueB:�*
dtype0
E
Const_32Const*%
valueB	"               *
dtype0	
i
StatelessRandomUniform_4StatelessRandomUniformConst_31Const_32*
T0*
Tseed0	*
dtype0
9
Const_33Const*
valueB 2>,p� �?*
dtype0
@
Cast_9CastConst_33*

SrcT0*
Truncate( *

DstT0
K
Init_conv2d_8_conv2d_biasMulStatelessRandomUniform_4Cast_9*
T0
�
Assign_conv2d_8_conv2d_biasAssignconv2d_8_conv2d_biasInit_conv2d_8_conv2d_bias*
use_locking(*
T0*
validate_shape(
�
Conv2d_4Conv2D	MaxPool_1conv2d_8_conv2d_kernel*
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
	BiasAdd_4BiasAddConv2d_4conv2d_8_conv2d_bias*
T0*
data_formatNHWC
"
Relu_4Relu	BiasAdd_4*
T0
0
Activation_conv2d_8IdentityRelu_4*
T0
t
conv2d_9_conv2d_kernel
VariableV2*
shape:��*
shared_name *
dtype0*
	container 
E
Const_34Const*%
valueB"      �   �   *
dtype0
E
Const_35Const*%
valueB	"               *
dtype0	
m
StatelessTruncatedNormal_5StatelessTruncatedNormalConst_34Const_35*
T0*
Tseed0	*
dtype0
9
Const_36Const*
valueB 2�V��@�?*
dtype0
A
Cast_10CastConst_36*

SrcT0*
Truncate( *

DstT0
P
Init_conv2d_9_conv2d_kernelMulStatelessTruncatedNormal_5Cast_10*
T0
�
Assign_conv2d_9_conv2d_kernelAssignconv2d_9_conv2d_kernelInit_conv2d_9_conv2d_kernel*
use_locking(*
T0*
validate_shape(
e
conv2d_9_conv2d_bias
VariableV2*
shape:�*
shared_name *
dtype0*
	container 
7
Const_37Const*
valueB:�*
dtype0
E
Const_38Const*%
valueB	"               *
dtype0	
i
StatelessRandomUniform_5StatelessRandomUniformConst_37Const_38*
T0*
Tseed0	*
dtype0
9
Const_39Const*
valueB 23�E�y�?*
dtype0
A
Cast_11CastConst_39*

SrcT0*
Truncate( *

DstT0
L
Init_conv2d_9_conv2d_biasMulStatelessRandomUniform_5Cast_11*
T0
�
Assign_conv2d_9_conv2d_biasAssignconv2d_9_conv2d_biasInit_conv2d_9_conv2d_bias*
use_locking(*
T0*
validate_shape(
�
Conv2d_5Conv2DActivation_conv2d_8conv2d_9_conv2d_kernel*
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
	BiasAdd_5BiasAddConv2d_5conv2d_9_conv2d_bias*
T0*
data_formatNHWC
"
Relu_5Relu	BiasAdd_5*
T0
0
Activation_conv2d_9IdentityRelu_5*
T0
E
Const_40Const*%
valueB"            *
dtype0
E
Const_41Const*%
valueB"            *
dtype0
s
	MaxPool_2	MaxPoolV2Activation_conv2d_9Const_40Const_41*
paddingVALID*
T0*
data_formatNHWC
u
conv2d_11_conv2d_kernel
VariableV2*
shape:��*
shared_name *
dtype0*
	container 
E
Const_42Const*%
valueB"      �      *
dtype0
E
Const_43Const*%
valueB	"               *
dtype0	
m
StatelessTruncatedNormal_6StatelessTruncatedNormalConst_42Const_43*
T0*
Tseed0	*
dtype0
9
Const_44Const*
valueB 2�V��@�?*
dtype0
A
Cast_12CastConst_44*

SrcT0*
Truncate( *

DstT0
Q
Init_conv2d_11_conv2d_kernelMulStatelessTruncatedNormal_6Cast_12*
T0
�
Assign_conv2d_11_conv2d_kernelAssignconv2d_11_conv2d_kernelInit_conv2d_11_conv2d_kernel*
use_locking(*
T0*
validate_shape(
f
conv2d_11_conv2d_bias
VariableV2*
shape:�*
shared_name *
dtype0*
	container 
7
Const_45Const*
valueB:�*
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
valueB 23�E�y�?*
dtype0
A
Cast_13CastConst_47*

SrcT0*
Truncate( *

DstT0
M
Init_conv2d_11_conv2d_biasMulStatelessRandomUniform_6Cast_13*
T0
�
Assign_conv2d_11_conv2d_biasAssignconv2d_11_conv2d_biasInit_conv2d_11_conv2d_bias*
use_locking(*
T0*
validate_shape(
�
Conv2d_6Conv2D	MaxPool_2conv2d_11_conv2d_kernel*
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
U
	BiasAdd_6BiasAddConv2d_6conv2d_11_conv2d_bias*
T0*
data_formatNHWC
"
Relu_6Relu	BiasAdd_6*
T0
1
Activation_conv2d_11IdentityRelu_6*
T0
u
conv2d_12_conv2d_kernel
VariableV2*
shape:��*
shared_name *
dtype0*
	container 
E
Const_48Const*%
valueB"            *
dtype0
E
Const_49Const*%
valueB	"               *
dtype0	
m
StatelessTruncatedNormal_7StatelessTruncatedNormalConst_48Const_49*
T0*
Tseed0	*
dtype0
9
Const_50Const*
valueB 2�m�7&�?*
dtype0
A
Cast_14CastConst_50*

SrcT0*
Truncate( *

DstT0
Q
Init_conv2d_12_conv2d_kernelMulStatelessTruncatedNormal_7Cast_14*
T0
�
Assign_conv2d_12_conv2d_kernelAssignconv2d_12_conv2d_kernelInit_conv2d_12_conv2d_kernel*
use_locking(*
T0*
validate_shape(
f
conv2d_12_conv2d_bias
VariableV2*
shape:�*
shared_name *
dtype0*
	container 
7
Const_51Const*
valueB:�*
dtype0
E
Const_52Const*%
valueB	"               *
dtype0	
i
StatelessRandomUniform_7StatelessRandomUniformConst_51Const_52*
T0*
Tseed0	*
dtype0
9
Const_53Const*
valueB 2>,p� �?*
dtype0
A
Cast_15CastConst_53*

SrcT0*
Truncate( *

DstT0
M
Init_conv2d_12_conv2d_biasMulStatelessRandomUniform_7Cast_15*
T0
�
Assign_conv2d_12_conv2d_biasAssignconv2d_12_conv2d_biasInit_conv2d_12_conv2d_bias*
use_locking(*
T0*
validate_shape(
�
Conv2d_7Conv2DActivation_conv2d_11conv2d_12_conv2d_kernel*
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
U
	BiasAdd_7BiasAddConv2d_7conv2d_12_conv2d_bias*
T0*
data_formatNHWC
"
Relu_7Relu	BiasAdd_7*
T0
1
Activation_conv2d_12IdentityRelu_7*
T0
E
Const_54Const*%
valueB"            *
dtype0
E
Const_55Const*%
valueB"            *
dtype0
t
	MaxPool_3	MaxPoolV2Activation_conv2d_12Const_54Const_55*
paddingVALID*
T0*
data_formatNHWC
=
Const_56Const*
valueB"���� @  *
dtype0
>
ReshapeReshape	MaxPool_3Const_56*
T0*
Tshape0
k
dense_15_dense_kernel
VariableV2*
shape:
��@*
shared_name *
dtype0*
	container 
=
Const_57Const*
valueB" @  @   *
dtype0
E
Const_58Const*%
valueB	"               *
dtype0	
m
StatelessTruncatedNormal_8StatelessTruncatedNormalConst_57Const_58*
T0*
Tseed0	*
dtype0
9
Const_59Const*
valueB 2��b�R��?*
dtype0
A
Cast_16CastConst_59*

SrcT0*
Truncate( *

DstT0
O
Init_dense_15_dense_kernelMulStatelessTruncatedNormal_8Cast_16*
T0
�
Assign_dense_15_dense_kernelAssigndense_15_dense_kernelInit_dense_15_dense_kernel*
use_locking(*
T0*
validate_shape(
c
dense_15_dense_bias
VariableV2*
shape:@*
shared_name *
dtype0*
	container 
6
Const_60Const*
valueB:@*
dtype0
E
Const_61Const*%
valueB	"               *
dtype0	
i
StatelessRandomUniform_8StatelessRandomUniformConst_60Const_61*
T0*
Tseed0	*
dtype0
9
Const_62Const*
valueB 2.!	���?*
dtype0
A
Cast_17CastConst_62*

SrcT0*
Truncate( *

DstT0
K
Init_dense_15_dense_biasMulStatelessRandomUniform_8Cast_17*
T0
�
Assign_dense_15_dense_biasAssigndense_15_dense_biasInit_dense_15_dense_bias*
use_locking(*
T0*
validate_shape(
_
MatMulMatMulReshapedense_15_dense_kernel*
transpose_b( *
T0*
transpose_a( 
0
AddAddMatMuldense_15_dense_bias*
T0

Relu_8ReluAdd*
T0
0
Activation_dense_15IdentityRelu_8*
T0
i
dense_17_dense_kernel
VariableV2*
shape
:@@*
shared_name *
dtype0*
	container 
=
Const_63Const*
valueB"@   @   *
dtype0
E
Const_64Const*%
valueB	"               *
dtype0	
m
StatelessTruncatedNormal_9StatelessTruncatedNormalConst_63Const_64*
T0*
Tseed0	*
dtype0
9
Const_65Const*
valueB 2��b�R��?*
dtype0
A
Cast_18CastConst_65*

SrcT0*
Truncate( *

DstT0
O
Init_dense_17_dense_kernelMulStatelessTruncatedNormal_9Cast_18*
T0
�
Assign_dense_17_dense_kernelAssigndense_17_dense_kernelInit_dense_17_dense_kernel*
use_locking(*
T0*
validate_shape(
c
dense_17_dense_bias
VariableV2*
shape:@*
shared_name *
dtype0*
	container 
6
Const_66Const*
valueB:@*
dtype0
E
Const_67Const*%
valueB	"               *
dtype0	
i
StatelessRandomUniform_9StatelessRandomUniformConst_66Const_67*
T0*
Tseed0	*
dtype0
9
Const_68Const*
valueB 2.!	���?*
dtype0
A
Cast_19CastConst_68*

SrcT0*
Truncate( *

DstT0
K
Init_dense_17_dense_biasMulStatelessRandomUniform_9Cast_19*
T0
�
Assign_dense_17_dense_biasAssigndense_17_dense_biasInit_dense_17_dense_bias*
use_locking(*
T0*
validate_shape(
m
MatMul_1MatMulActivation_dense_15dense_17_dense_kernel*
transpose_b( *
T0*
transpose_a( 
4
Add_1AddMatMul_1dense_17_dense_bias*
T0

Relu_9ReluAdd_1*
T0
0
Activation_dense_17IdentityRelu_9*
T0
i
dense_19_dense_kernel
VariableV2*
shape
:@*
shared_name *
dtype0*
	container 
=
Const_69Const*
valueB"@      *
dtype0
E
Const_70Const*%
valueB	"               *
dtype0	
j
StatelessRandomUniform_10StatelessRandomUniformConst_69Const_70*
T0*
Tseed0	*
dtype0
9
Const_71Const*
valueB 2��Oy���?*
dtype0
A
Cast_20CastConst_71*

SrcT0*
Truncate( *

DstT0
N
Init_dense_19_dense_kernelMulStatelessRandomUniform_10Cast_20*
T0
�
Assign_dense_19_dense_kernelAssigndense_19_dense_kernelInit_dense_19_dense_kernel*
use_locking(*
T0*
validate_shape(
c
dense_19_dense_bias
VariableV2*
shape:*
shared_name *
dtype0*
	container 
6
Const_72Const*
valueB:*
dtype0
E
Const_73Const*%
valueB	"               *
dtype0	
j
StatelessRandomUniform_11StatelessRandomUniformConst_72Const_73*
T0*
Tseed0	*
dtype0
9
Const_74Const*
valueB 2.!	���?*
dtype0
A
Cast_21CastConst_74*

SrcT0*
Truncate( *

DstT0
L
Init_dense_19_dense_biasMulStatelessRandomUniform_11Cast_21*
T0
�
Assign_dense_19_dense_biasAssigndense_19_dense_biasInit_dense_19_dense_bias*
use_locking(*
T0*
validate_shape(
m
MatMul_2MatMulActivation_dense_17dense_19_dense_kernel*
transpose_b( *
T0*
transpose_a( 
4
Add_2AddMatMul_2dense_19_dense_bias*
T0
"
SoftmaxSoftmaxAdd_2*
T0
1
Activation_dense_19IdentitySoftmax*
T0
6
PlaceholderPlaceholder*
shape:*
dtype0
Q
SquaredDifferenceSquaredDifferenceActivation_dense_19Placeholder*
T0
;
Const_75Const*
valueB :
���������*
dtype0
O
MeanMeanSquaredDifferenceConst_75*

Tidx0*
	keep_dims( *
T0
2
Const_76Const*
value	B : *
dtype0

RankRankMean*
T0
2
Const_77Const*
value	B :*
dtype0
4
RangeRangeConst_76RankConst_77*

Tidx0
C
	ReduceSumSumMeanRange*

Tidx0*
	keep_dims( *
T0
2
Const_78Const*
value	B : *
dtype0
*
Rank_1RankSquaredDifference*
T0
2
Const_79Const*
value	B :*
dtype0
8
Range_1RangeConst_78Rank_1Const_79*

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
Gradients/SubtractSubActivation_dense_19Placeholder*
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
Gradients/Shape_3ShapeActivation_dense_19*
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
Gradients/Shape_6Shapedense_19_dense_bias*
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
Gradients/MatMulMatMulGradients/Reshape_6dense_19_dense_kernel*
transpose_b(*
T0*
transpose_a( 
u
Gradients/MatMul_1MatMulActivation_dense_17Gradients/Reshape_6*
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
Gradients/Shape_8Shapedense_17_dense_bias*
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
Gradients/MatMul_2MatMulGradients/Reshape_8dense_17_dense_kernel*
transpose_b(*
T0*
transpose_a( 
u
Gradients/MatMul_3MatMulActivation_dense_15Gradients/Reshape_8*
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
Gradients/Shape_10Shapedense_15_dense_bias*
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
Gradients/MatMul_4MatMulGradients/Reshape_10dense_15_dense_kernel*
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
Gradients/MaxPoolGradV2MaxPoolGradV2Activation_conv2d_12	MaxPool_3Gradients/Reshape_12Const_54Const_55*
paddingVALID*
T0*
data_formatNHWC
C
Gradients/Identity_10IdentityGradients/MaxPoolGradV2*
T0
K
Gradients/ReluGrad_2ReluGradGradients/Identity_10	BiasAdd_7*
T0
Z
Gradients/BiasAddGradBiasAddGradGradients/ReluGrad_2*
T0*
data_formatNHWC
@
Gradients/Identity_11IdentityGradients/ReluGrad_2*
T0
J
Gradients/Shape_12ShapeActivation_conv2d_11*
T0*
out_type0
�
Gradients/Conv2DBackpropInputConv2DBackpropInputGradients/Shape_12conv2d_12_conv2d_kernelGradients/Identity_11*
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
M
Gradients/Shape_13Shapeconv2d_12_conv2d_kernel*
T0*
out_type0
�
Gradients/Conv2DBackpropFilterConv2DBackpropFilterActivation_conv2d_11Gradients/Shape_13Gradients/Identity_11*
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
I
Gradients/Identity_12IdentityGradients/Conv2DBackpropInput*
T0
K
Gradients/ReluGrad_3ReluGradGradients/Identity_12	BiasAdd_6*
T0
\
Gradients/BiasAddGrad_1BiasAddGradGradients/ReluGrad_3*
T0*
data_formatNHWC
@
Gradients/Identity_13IdentityGradients/ReluGrad_3*
T0
?
Gradients/Shape_14Shape	MaxPool_2*
T0*
out_type0
�
Gradients/Conv2DBackpropInput_1Conv2DBackpropInputGradients/Shape_14conv2d_11_conv2d_kernelGradients/Identity_13*
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
M
Gradients/Shape_15Shapeconv2d_11_conv2d_kernel*
T0*
out_type0
�
 Gradients/Conv2DBackpropFilter_1Conv2DBackpropFilter	MaxPool_2Gradients/Shape_15Gradients/Identity_13*
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
Gradients/MaxPoolGradV2_1MaxPoolGradV2Activation_conv2d_9	MaxPool_2Gradients/Conv2DBackpropInput_1Const_40Const_41*
paddingVALID*
T0*
data_formatNHWC
E
Gradients/Identity_14IdentityGradients/MaxPoolGradV2_1*
T0
K
Gradients/ReluGrad_4ReluGradGradients/Identity_14	BiasAdd_5*
T0
\
Gradients/BiasAddGrad_2BiasAddGradGradients/ReluGrad_4*
T0*
data_formatNHWC
@
Gradients/Identity_15IdentityGradients/ReluGrad_4*
T0
I
Gradients/Shape_16ShapeActivation_conv2d_8*
T0*
out_type0
�
Gradients/Conv2DBackpropInput_2Conv2DBackpropInputGradients/Shape_16conv2d_9_conv2d_kernelGradients/Identity_15*
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
Gradients/Shape_17Shapeconv2d_9_conv2d_kernel*
T0*
out_type0
�
 Gradients/Conv2DBackpropFilter_2Conv2DBackpropFilterActivation_conv2d_8Gradients/Shape_17Gradients/Identity_15*
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
K
Gradients/Identity_16IdentityGradients/Conv2DBackpropInput_2*
T0
K
Gradients/ReluGrad_5ReluGradGradients/Identity_16	BiasAdd_4*
T0
\
Gradients/BiasAddGrad_3BiasAddGradGradients/ReluGrad_5*
T0*
data_formatNHWC
@
Gradients/Identity_17IdentityGradients/ReluGrad_5*
T0
?
Gradients/Shape_18Shape	MaxPool_1*
T0*
out_type0
�
Gradients/Conv2DBackpropInput_3Conv2DBackpropInputGradients/Shape_18conv2d_8_conv2d_kernelGradients/Identity_17*
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
Gradients/Shape_19Shapeconv2d_8_conv2d_kernel*
T0*
out_type0
�
 Gradients/Conv2DBackpropFilter_3Conv2DBackpropFilter	MaxPool_1Gradients/Shape_19Gradients/Identity_17*
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
Gradients/MaxPoolGradV2_2MaxPoolGradV2Activation_conv2d_6	MaxPool_1Gradients/Conv2DBackpropInput_3Const_26Const_27*
paddingVALID*
T0*
data_formatNHWC
E
Gradients/Identity_18IdentityGradients/MaxPoolGradV2_2*
T0
K
Gradients/ReluGrad_6ReluGradGradients/Identity_18	BiasAdd_3*
T0
\
Gradients/BiasAddGrad_4BiasAddGradGradients/ReluGrad_6*
T0*
data_formatNHWC
@
Gradients/Identity_19IdentityGradients/ReluGrad_6*
T0
I
Gradients/Shape_20ShapeActivation_conv2d_5*
T0*
out_type0
�
Gradients/Conv2DBackpropInput_4Conv2DBackpropInputGradients/Shape_20conv2d_6_conv2d_kernelGradients/Identity_19*
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
Gradients/Shape_21Shapeconv2d_6_conv2d_kernel*
T0*
out_type0
�
 Gradients/Conv2DBackpropFilter_4Conv2DBackpropFilterActivation_conv2d_5Gradients/Shape_21Gradients/Identity_19*
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
K
Gradients/Identity_20IdentityGradients/Conv2DBackpropInput_4*
T0
K
Gradients/ReluGrad_7ReluGradGradients/Identity_20	BiasAdd_2*
T0
\
Gradients/BiasAddGrad_5BiasAddGradGradients/ReluGrad_7*
T0*
data_formatNHWC
@
Gradients/Identity_21IdentityGradients/ReluGrad_7*
T0
=
Gradients/Shape_22ShapeMaxPool*
T0*
out_type0
�
Gradients/Conv2DBackpropInput_5Conv2DBackpropInputGradients/Shape_22conv2d_5_conv2d_kernelGradients/Identity_21*
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
Gradients/Shape_23Shapeconv2d_5_conv2d_kernel*
T0*
out_type0
�
 Gradients/Conv2DBackpropFilter_5Conv2DBackpropFilterMaxPoolGradients/Shape_23Gradients/Identity_21*
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
Gradients/MaxPoolGradV2_3MaxPoolGradV2Activation_conv2d_3MaxPoolGradients/Conv2DBackpropInput_5Const_12Const_13*
paddingVALID*
T0*
data_formatNHWC
E
Gradients/Identity_22IdentityGradients/MaxPoolGradV2_3*
T0
K
Gradients/ReluGrad_8ReluGradGradients/Identity_22	BiasAdd_1*
T0
\
Gradients/BiasAddGrad_6BiasAddGradGradients/ReluGrad_8*
T0*
data_formatNHWC
@
Gradients/Identity_23IdentityGradients/ReluGrad_8*
T0
I
Gradients/Shape_24ShapeActivation_conv2d_2*
T0*
out_type0
�
Gradients/Conv2DBackpropInput_6Conv2DBackpropInputGradients/Shape_24conv2d_3_conv2d_kernelGradients/Identity_23*
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
Gradients/Shape_25Shapeconv2d_3_conv2d_kernel*
T0*
out_type0
�
 Gradients/Conv2DBackpropFilter_6Conv2DBackpropFilterActivation_conv2d_2Gradients/Shape_25Gradients/Identity_23*
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
K
Gradients/Identity_24IdentityGradients/Conv2DBackpropInput_6*
T0
I
Gradients/ReluGrad_9ReluGradGradients/Identity_24BiasAdd*
T0
\
Gradients/BiasAddGrad_7BiasAddGradGradients/ReluGrad_9*
T0*
data_formatNHWC
@
Gradients/Identity_25IdentityGradients/ReluGrad_9*
T0
N
Gradients/Shape_26Shapedefault_data_placeholder*
T0*
out_type0
�
Gradients/Conv2DBackpropInput_7Conv2DBackpropInputGradients/Shape_26conv2d_2_conv2d_kernelGradients/Identity_25*
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
Gradients/Shape_27Shapeconv2d_2_conv2d_kernel*
T0*
out_type0
�
 Gradients/Conv2DBackpropFilter_7Conv2DBackpropFilterdefault_data_placeholderGradients/Shape_27Gradients/Identity_25*
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
Const_80Const*
valueB
 *    *
dtype0
[
'Init_optimizer_conv2d_2_conv2d_kernel-mFillShapeConst_80*
T0*

index_type0
~
"optimizer_conv2d_2_conv2d_kernel-m
VariableV2*
shape: *
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
Const_81Const*
valueB
 *    *
dtype0
]
'Init_optimizer_conv2d_2_conv2d_kernel-vFillShape_1Const_81*
T0*

index_type0
~
"optimizer_conv2d_2_conv2d_kernel-v
VariableV2*
shape: *
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
Const_82Const*
valueB
 *    *
dtype0
[
%Init_optimizer_conv2d_2_conv2d_bias-mFillShape_2Const_82*
T0*

index_type0
p
 optimizer_conv2d_2_conv2d_bias-m
VariableV2*
shape: *
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
Const_83Const*
valueB
 *    *
dtype0
[
%Init_optimizer_conv2d_2_conv2d_bias-vFillShape_3Const_83*
T0*

index_type0
p
 optimizer_conv2d_2_conv2d_bias-v
VariableV2*
shape: *
shared_name *
dtype0*
	container 
�
'Assign_optimizer_conv2d_2_conv2d_bias-vAssign optimizer_conv2d_2_conv2d_bias-v%Init_optimizer_conv2d_2_conv2d_bias-v*
use_locking(*
T0*
validate_shape(
A
Shape_4Shapeconv2d_3_conv2d_kernel*
T0*
out_type0
5
Const_84Const*
valueB
 *    *
dtype0
]
'Init_optimizer_conv2d_3_conv2d_kernel-mFillShape_4Const_84*
T0*

index_type0
~
"optimizer_conv2d_3_conv2d_kernel-m
VariableV2*
shape:  *
shared_name *
dtype0*
	container 
�
)Assign_optimizer_conv2d_3_conv2d_kernel-mAssign"optimizer_conv2d_3_conv2d_kernel-m'Init_optimizer_conv2d_3_conv2d_kernel-m*
use_locking(*
T0*
validate_shape(
A
Shape_5Shapeconv2d_3_conv2d_kernel*
T0*
out_type0
5
Const_85Const*
valueB
 *    *
dtype0
]
'Init_optimizer_conv2d_3_conv2d_kernel-vFillShape_5Const_85*
T0*

index_type0
~
"optimizer_conv2d_3_conv2d_kernel-v
VariableV2*
shape:  *
shared_name *
dtype0*
	container 
�
)Assign_optimizer_conv2d_3_conv2d_kernel-vAssign"optimizer_conv2d_3_conv2d_kernel-v'Init_optimizer_conv2d_3_conv2d_kernel-v*
use_locking(*
T0*
validate_shape(
?
Shape_6Shapeconv2d_3_conv2d_bias*
T0*
out_type0
5
Const_86Const*
valueB
 *    *
dtype0
[
%Init_optimizer_conv2d_3_conv2d_bias-mFillShape_6Const_86*
T0*

index_type0
p
 optimizer_conv2d_3_conv2d_bias-m
VariableV2*
shape: *
shared_name *
dtype0*
	container 
�
'Assign_optimizer_conv2d_3_conv2d_bias-mAssign optimizer_conv2d_3_conv2d_bias-m%Init_optimizer_conv2d_3_conv2d_bias-m*
use_locking(*
T0*
validate_shape(
?
Shape_7Shapeconv2d_3_conv2d_bias*
T0*
out_type0
5
Const_87Const*
valueB
 *    *
dtype0
[
%Init_optimizer_conv2d_3_conv2d_bias-vFillShape_7Const_87*
T0*

index_type0
p
 optimizer_conv2d_3_conv2d_bias-v
VariableV2*
shape: *
shared_name *
dtype0*
	container 
�
'Assign_optimizer_conv2d_3_conv2d_bias-vAssign optimizer_conv2d_3_conv2d_bias-v%Init_optimizer_conv2d_3_conv2d_bias-v*
use_locking(*
T0*
validate_shape(
A
Shape_8Shapeconv2d_5_conv2d_kernel*
T0*
out_type0
5
Const_88Const*
valueB
 *    *
dtype0
]
'Init_optimizer_conv2d_5_conv2d_kernel-mFillShape_8Const_88*
T0*

index_type0
~
"optimizer_conv2d_5_conv2d_kernel-m
VariableV2*
shape: @*
shared_name *
dtype0*
	container 
�
)Assign_optimizer_conv2d_5_conv2d_kernel-mAssign"optimizer_conv2d_5_conv2d_kernel-m'Init_optimizer_conv2d_5_conv2d_kernel-m*
use_locking(*
T0*
validate_shape(
A
Shape_9Shapeconv2d_5_conv2d_kernel*
T0*
out_type0
5
Const_89Const*
valueB
 *    *
dtype0
]
'Init_optimizer_conv2d_5_conv2d_kernel-vFillShape_9Const_89*
T0*

index_type0
~
"optimizer_conv2d_5_conv2d_kernel-v
VariableV2*
shape: @*
shared_name *
dtype0*
	container 
�
)Assign_optimizer_conv2d_5_conv2d_kernel-vAssign"optimizer_conv2d_5_conv2d_kernel-v'Init_optimizer_conv2d_5_conv2d_kernel-v*
use_locking(*
T0*
validate_shape(
@
Shape_10Shapeconv2d_5_conv2d_bias*
T0*
out_type0
5
Const_90Const*
valueB
 *    *
dtype0
\
%Init_optimizer_conv2d_5_conv2d_bias-mFillShape_10Const_90*
T0*

index_type0
p
 optimizer_conv2d_5_conv2d_bias-m
VariableV2*
shape:@*
shared_name *
dtype0*
	container 
�
'Assign_optimizer_conv2d_5_conv2d_bias-mAssign optimizer_conv2d_5_conv2d_bias-m%Init_optimizer_conv2d_5_conv2d_bias-m*
use_locking(*
T0*
validate_shape(
@
Shape_11Shapeconv2d_5_conv2d_bias*
T0*
out_type0
5
Const_91Const*
valueB
 *    *
dtype0
\
%Init_optimizer_conv2d_5_conv2d_bias-vFillShape_11Const_91*
T0*

index_type0
p
 optimizer_conv2d_5_conv2d_bias-v
VariableV2*
shape:@*
shared_name *
dtype0*
	container 
�
'Assign_optimizer_conv2d_5_conv2d_bias-vAssign optimizer_conv2d_5_conv2d_bias-v%Init_optimizer_conv2d_5_conv2d_bias-v*
use_locking(*
T0*
validate_shape(
B
Shape_12Shapeconv2d_6_conv2d_kernel*
T0*
out_type0
5
Const_92Const*
valueB
 *    *
dtype0
^
'Init_optimizer_conv2d_6_conv2d_kernel-mFillShape_12Const_92*
T0*

index_type0
~
"optimizer_conv2d_6_conv2d_kernel-m
VariableV2*
shape:@@*
shared_name *
dtype0*
	container 
�
)Assign_optimizer_conv2d_6_conv2d_kernel-mAssign"optimizer_conv2d_6_conv2d_kernel-m'Init_optimizer_conv2d_6_conv2d_kernel-m*
use_locking(*
T0*
validate_shape(
B
Shape_13Shapeconv2d_6_conv2d_kernel*
T0*
out_type0
5
Const_93Const*
valueB
 *    *
dtype0
^
'Init_optimizer_conv2d_6_conv2d_kernel-vFillShape_13Const_93*
T0*

index_type0
~
"optimizer_conv2d_6_conv2d_kernel-v
VariableV2*
shape:@@*
shared_name *
dtype0*
	container 
�
)Assign_optimizer_conv2d_6_conv2d_kernel-vAssign"optimizer_conv2d_6_conv2d_kernel-v'Init_optimizer_conv2d_6_conv2d_kernel-v*
use_locking(*
T0*
validate_shape(
@
Shape_14Shapeconv2d_6_conv2d_bias*
T0*
out_type0
5
Const_94Const*
valueB
 *    *
dtype0
\
%Init_optimizer_conv2d_6_conv2d_bias-mFillShape_14Const_94*
T0*

index_type0
p
 optimizer_conv2d_6_conv2d_bias-m
VariableV2*
shape:@*
shared_name *
dtype0*
	container 
�
'Assign_optimizer_conv2d_6_conv2d_bias-mAssign optimizer_conv2d_6_conv2d_bias-m%Init_optimizer_conv2d_6_conv2d_bias-m*
use_locking(*
T0*
validate_shape(
@
Shape_15Shapeconv2d_6_conv2d_bias*
T0*
out_type0
5
Const_95Const*
valueB
 *    *
dtype0
\
%Init_optimizer_conv2d_6_conv2d_bias-vFillShape_15Const_95*
T0*

index_type0
p
 optimizer_conv2d_6_conv2d_bias-v
VariableV2*
shape:@*
shared_name *
dtype0*
	container 
�
'Assign_optimizer_conv2d_6_conv2d_bias-vAssign optimizer_conv2d_6_conv2d_bias-v%Init_optimizer_conv2d_6_conv2d_bias-v*
use_locking(*
T0*
validate_shape(
B
Shape_16Shapeconv2d_8_conv2d_kernel*
T0*
out_type0
5
Const_96Const*
valueB
 *    *
dtype0
^
'Init_optimizer_conv2d_8_conv2d_kernel-mFillShape_16Const_96*
T0*

index_type0

"optimizer_conv2d_8_conv2d_kernel-m
VariableV2*
shape:@�*
shared_name *
dtype0*
	container 
�
)Assign_optimizer_conv2d_8_conv2d_kernel-mAssign"optimizer_conv2d_8_conv2d_kernel-m'Init_optimizer_conv2d_8_conv2d_kernel-m*
use_locking(*
T0*
validate_shape(
B
Shape_17Shapeconv2d_8_conv2d_kernel*
T0*
out_type0
5
Const_97Const*
valueB
 *    *
dtype0
^
'Init_optimizer_conv2d_8_conv2d_kernel-vFillShape_17Const_97*
T0*

index_type0

"optimizer_conv2d_8_conv2d_kernel-v
VariableV2*
shape:@�*
shared_name *
dtype0*
	container 
�
)Assign_optimizer_conv2d_8_conv2d_kernel-vAssign"optimizer_conv2d_8_conv2d_kernel-v'Init_optimizer_conv2d_8_conv2d_kernel-v*
use_locking(*
T0*
validate_shape(
@
Shape_18Shapeconv2d_8_conv2d_bias*
T0*
out_type0
5
Const_98Const*
valueB
 *    *
dtype0
\
%Init_optimizer_conv2d_8_conv2d_bias-mFillShape_18Const_98*
T0*

index_type0
q
 optimizer_conv2d_8_conv2d_bias-m
VariableV2*
shape:�*
shared_name *
dtype0*
	container 
�
'Assign_optimizer_conv2d_8_conv2d_bias-mAssign optimizer_conv2d_8_conv2d_bias-m%Init_optimizer_conv2d_8_conv2d_bias-m*
use_locking(*
T0*
validate_shape(
@
Shape_19Shapeconv2d_8_conv2d_bias*
T0*
out_type0
5
Const_99Const*
valueB
 *    *
dtype0
\
%Init_optimizer_conv2d_8_conv2d_bias-vFillShape_19Const_99*
T0*

index_type0
q
 optimizer_conv2d_8_conv2d_bias-v
VariableV2*
shape:�*
shared_name *
dtype0*
	container 
�
'Assign_optimizer_conv2d_8_conv2d_bias-vAssign optimizer_conv2d_8_conv2d_bias-v%Init_optimizer_conv2d_8_conv2d_bias-v*
use_locking(*
T0*
validate_shape(
B
Shape_20Shapeconv2d_9_conv2d_kernel*
T0*
out_type0
6
	Const_100Const*
valueB
 *    *
dtype0
_
'Init_optimizer_conv2d_9_conv2d_kernel-mFillShape_20	Const_100*
T0*

index_type0
�
"optimizer_conv2d_9_conv2d_kernel-m
VariableV2*
shape:��*
shared_name *
dtype0*
	container 
�
)Assign_optimizer_conv2d_9_conv2d_kernel-mAssign"optimizer_conv2d_9_conv2d_kernel-m'Init_optimizer_conv2d_9_conv2d_kernel-m*
use_locking(*
T0*
validate_shape(
B
Shape_21Shapeconv2d_9_conv2d_kernel*
T0*
out_type0
6
	Const_101Const*
valueB
 *    *
dtype0
_
'Init_optimizer_conv2d_9_conv2d_kernel-vFillShape_21	Const_101*
T0*

index_type0
�
"optimizer_conv2d_9_conv2d_kernel-v
VariableV2*
shape:��*
shared_name *
dtype0*
	container 
�
)Assign_optimizer_conv2d_9_conv2d_kernel-vAssign"optimizer_conv2d_9_conv2d_kernel-v'Init_optimizer_conv2d_9_conv2d_kernel-v*
use_locking(*
T0*
validate_shape(
@
Shape_22Shapeconv2d_9_conv2d_bias*
T0*
out_type0
6
	Const_102Const*
valueB
 *    *
dtype0
]
%Init_optimizer_conv2d_9_conv2d_bias-mFillShape_22	Const_102*
T0*

index_type0
q
 optimizer_conv2d_9_conv2d_bias-m
VariableV2*
shape:�*
shared_name *
dtype0*
	container 
�
'Assign_optimizer_conv2d_9_conv2d_bias-mAssign optimizer_conv2d_9_conv2d_bias-m%Init_optimizer_conv2d_9_conv2d_bias-m*
use_locking(*
T0*
validate_shape(
@
Shape_23Shapeconv2d_9_conv2d_bias*
T0*
out_type0
6
	Const_103Const*
valueB
 *    *
dtype0
]
%Init_optimizer_conv2d_9_conv2d_bias-vFillShape_23	Const_103*
T0*

index_type0
q
 optimizer_conv2d_9_conv2d_bias-v
VariableV2*
shape:�*
shared_name *
dtype0*
	container 
�
'Assign_optimizer_conv2d_9_conv2d_bias-vAssign optimizer_conv2d_9_conv2d_bias-v%Init_optimizer_conv2d_9_conv2d_bias-v*
use_locking(*
T0*
validate_shape(
C
Shape_24Shapeconv2d_11_conv2d_kernel*
T0*
out_type0
6
	Const_104Const*
valueB
 *    *
dtype0
`
(Init_optimizer_conv2d_11_conv2d_kernel-mFillShape_24	Const_104*
T0*

index_type0
�
#optimizer_conv2d_11_conv2d_kernel-m
VariableV2*
shape:��*
shared_name *
dtype0*
	container 
�
*Assign_optimizer_conv2d_11_conv2d_kernel-mAssign#optimizer_conv2d_11_conv2d_kernel-m(Init_optimizer_conv2d_11_conv2d_kernel-m*
use_locking(*
T0*
validate_shape(
C
Shape_25Shapeconv2d_11_conv2d_kernel*
T0*
out_type0
6
	Const_105Const*
valueB
 *    *
dtype0
`
(Init_optimizer_conv2d_11_conv2d_kernel-vFillShape_25	Const_105*
T0*

index_type0
�
#optimizer_conv2d_11_conv2d_kernel-v
VariableV2*
shape:��*
shared_name *
dtype0*
	container 
�
*Assign_optimizer_conv2d_11_conv2d_kernel-vAssign#optimizer_conv2d_11_conv2d_kernel-v(Init_optimizer_conv2d_11_conv2d_kernel-v*
use_locking(*
T0*
validate_shape(
A
Shape_26Shapeconv2d_11_conv2d_bias*
T0*
out_type0
6
	Const_106Const*
valueB
 *    *
dtype0
^
&Init_optimizer_conv2d_11_conv2d_bias-mFillShape_26	Const_106*
T0*

index_type0
r
!optimizer_conv2d_11_conv2d_bias-m
VariableV2*
shape:�*
shared_name *
dtype0*
	container 
�
(Assign_optimizer_conv2d_11_conv2d_bias-mAssign!optimizer_conv2d_11_conv2d_bias-m&Init_optimizer_conv2d_11_conv2d_bias-m*
use_locking(*
T0*
validate_shape(
A
Shape_27Shapeconv2d_11_conv2d_bias*
T0*
out_type0
6
	Const_107Const*
valueB
 *    *
dtype0
^
&Init_optimizer_conv2d_11_conv2d_bias-vFillShape_27	Const_107*
T0*

index_type0
r
!optimizer_conv2d_11_conv2d_bias-v
VariableV2*
shape:�*
shared_name *
dtype0*
	container 
�
(Assign_optimizer_conv2d_11_conv2d_bias-vAssign!optimizer_conv2d_11_conv2d_bias-v&Init_optimizer_conv2d_11_conv2d_bias-v*
use_locking(*
T0*
validate_shape(
C
Shape_28Shapeconv2d_12_conv2d_kernel*
T0*
out_type0
6
	Const_108Const*
valueB
 *    *
dtype0
`
(Init_optimizer_conv2d_12_conv2d_kernel-mFillShape_28	Const_108*
T0*

index_type0
�
#optimizer_conv2d_12_conv2d_kernel-m
VariableV2*
shape:��*
shared_name *
dtype0*
	container 
�
*Assign_optimizer_conv2d_12_conv2d_kernel-mAssign#optimizer_conv2d_12_conv2d_kernel-m(Init_optimizer_conv2d_12_conv2d_kernel-m*
use_locking(*
T0*
validate_shape(
C
Shape_29Shapeconv2d_12_conv2d_kernel*
T0*
out_type0
6
	Const_109Const*
valueB
 *    *
dtype0
`
(Init_optimizer_conv2d_12_conv2d_kernel-vFillShape_29	Const_109*
T0*

index_type0
�
#optimizer_conv2d_12_conv2d_kernel-v
VariableV2*
shape:��*
shared_name *
dtype0*
	container 
�
*Assign_optimizer_conv2d_12_conv2d_kernel-vAssign#optimizer_conv2d_12_conv2d_kernel-v(Init_optimizer_conv2d_12_conv2d_kernel-v*
use_locking(*
T0*
validate_shape(
A
Shape_30Shapeconv2d_12_conv2d_bias*
T0*
out_type0
6
	Const_110Const*
valueB
 *    *
dtype0
^
&Init_optimizer_conv2d_12_conv2d_bias-mFillShape_30	Const_110*
T0*

index_type0
r
!optimizer_conv2d_12_conv2d_bias-m
VariableV2*
shape:�*
shared_name *
dtype0*
	container 
�
(Assign_optimizer_conv2d_12_conv2d_bias-mAssign!optimizer_conv2d_12_conv2d_bias-m&Init_optimizer_conv2d_12_conv2d_bias-m*
use_locking(*
T0*
validate_shape(
A
Shape_31Shapeconv2d_12_conv2d_bias*
T0*
out_type0
6
	Const_111Const*
valueB
 *    *
dtype0
^
&Init_optimizer_conv2d_12_conv2d_bias-vFillShape_31	Const_111*
T0*

index_type0
r
!optimizer_conv2d_12_conv2d_bias-v
VariableV2*
shape:�*
shared_name *
dtype0*
	container 
�
(Assign_optimizer_conv2d_12_conv2d_bias-vAssign!optimizer_conv2d_12_conv2d_bias-v&Init_optimizer_conv2d_12_conv2d_bias-v*
use_locking(*
T0*
validate_shape(
A
Shape_32Shapedense_15_dense_kernel*
T0*
out_type0
6
	Const_112Const*
valueB
 *    *
dtype0
^
&Init_optimizer_dense_15_dense_kernel-mFillShape_32	Const_112*
T0*

index_type0
w
!optimizer_dense_15_dense_kernel-m
VariableV2*
shape:
��@*
shared_name *
dtype0*
	container 
�
(Assign_optimizer_dense_15_dense_kernel-mAssign!optimizer_dense_15_dense_kernel-m&Init_optimizer_dense_15_dense_kernel-m*
use_locking(*
T0*
validate_shape(
A
Shape_33Shapedense_15_dense_kernel*
T0*
out_type0
6
	Const_113Const*
valueB
 *    *
dtype0
^
&Init_optimizer_dense_15_dense_kernel-vFillShape_33	Const_113*
T0*

index_type0
w
!optimizer_dense_15_dense_kernel-v
VariableV2*
shape:
��@*
shared_name *
dtype0*
	container 
�
(Assign_optimizer_dense_15_dense_kernel-vAssign!optimizer_dense_15_dense_kernel-v&Init_optimizer_dense_15_dense_kernel-v*
use_locking(*
T0*
validate_shape(
?
Shape_34Shapedense_15_dense_bias*
T0*
out_type0
6
	Const_114Const*
valueB
 *    *
dtype0
\
$Init_optimizer_dense_15_dense_bias-mFillShape_34	Const_114*
T0*

index_type0
o
optimizer_dense_15_dense_bias-m
VariableV2*
shape:@*
shared_name *
dtype0*
	container 
�
&Assign_optimizer_dense_15_dense_bias-mAssignoptimizer_dense_15_dense_bias-m$Init_optimizer_dense_15_dense_bias-m*
use_locking(*
T0*
validate_shape(
?
Shape_35Shapedense_15_dense_bias*
T0*
out_type0
6
	Const_115Const*
valueB
 *    *
dtype0
\
$Init_optimizer_dense_15_dense_bias-vFillShape_35	Const_115*
T0*

index_type0
o
optimizer_dense_15_dense_bias-v
VariableV2*
shape:@*
shared_name *
dtype0*
	container 
�
&Assign_optimizer_dense_15_dense_bias-vAssignoptimizer_dense_15_dense_bias-v$Init_optimizer_dense_15_dense_bias-v*
use_locking(*
T0*
validate_shape(
A
Shape_36Shapedense_17_dense_kernel*
T0*
out_type0
6
	Const_116Const*
valueB
 *    *
dtype0
^
&Init_optimizer_dense_17_dense_kernel-mFillShape_36	Const_116*
T0*

index_type0
u
!optimizer_dense_17_dense_kernel-m
VariableV2*
shape
:@@*
shared_name *
dtype0*
	container 
�
(Assign_optimizer_dense_17_dense_kernel-mAssign!optimizer_dense_17_dense_kernel-m&Init_optimizer_dense_17_dense_kernel-m*
use_locking(*
T0*
validate_shape(
A
Shape_37Shapedense_17_dense_kernel*
T0*
out_type0
6
	Const_117Const*
valueB
 *    *
dtype0
^
&Init_optimizer_dense_17_dense_kernel-vFillShape_37	Const_117*
T0*

index_type0
u
!optimizer_dense_17_dense_kernel-v
VariableV2*
shape
:@@*
shared_name *
dtype0*
	container 
�
(Assign_optimizer_dense_17_dense_kernel-vAssign!optimizer_dense_17_dense_kernel-v&Init_optimizer_dense_17_dense_kernel-v*
use_locking(*
T0*
validate_shape(
?
Shape_38Shapedense_17_dense_bias*
T0*
out_type0
6
	Const_118Const*
valueB
 *    *
dtype0
\
$Init_optimizer_dense_17_dense_bias-mFillShape_38	Const_118*
T0*

index_type0
o
optimizer_dense_17_dense_bias-m
VariableV2*
shape:@*
shared_name *
dtype0*
	container 
�
&Assign_optimizer_dense_17_dense_bias-mAssignoptimizer_dense_17_dense_bias-m$Init_optimizer_dense_17_dense_bias-m*
use_locking(*
T0*
validate_shape(
?
Shape_39Shapedense_17_dense_bias*
T0*
out_type0
6
	Const_119Const*
valueB
 *    *
dtype0
\
$Init_optimizer_dense_17_dense_bias-vFillShape_39	Const_119*
T0*

index_type0
o
optimizer_dense_17_dense_bias-v
VariableV2*
shape:@*
shared_name *
dtype0*
	container 
�
&Assign_optimizer_dense_17_dense_bias-vAssignoptimizer_dense_17_dense_bias-v$Init_optimizer_dense_17_dense_bias-v*
use_locking(*
T0*
validate_shape(
A
Shape_40Shapedense_19_dense_kernel*
T0*
out_type0
6
	Const_120Const*
valueB
 *    *
dtype0
^
&Init_optimizer_dense_19_dense_kernel-mFillShape_40	Const_120*
T0*

index_type0
u
!optimizer_dense_19_dense_kernel-m
VariableV2*
shape
:@*
shared_name *
dtype0*
	container 
�
(Assign_optimizer_dense_19_dense_kernel-mAssign!optimizer_dense_19_dense_kernel-m&Init_optimizer_dense_19_dense_kernel-m*
use_locking(*
T0*
validate_shape(
A
Shape_41Shapedense_19_dense_kernel*
T0*
out_type0
6
	Const_121Const*
valueB
 *    *
dtype0
^
&Init_optimizer_dense_19_dense_kernel-vFillShape_41	Const_121*
T0*

index_type0
u
!optimizer_dense_19_dense_kernel-v
VariableV2*
shape
:@*
shared_name *
dtype0*
	container 
�
(Assign_optimizer_dense_19_dense_kernel-vAssign!optimizer_dense_19_dense_kernel-v&Init_optimizer_dense_19_dense_kernel-v*
use_locking(*
T0*
validate_shape(
?
Shape_42Shapedense_19_dense_bias*
T0*
out_type0
6
	Const_122Const*
valueB
 *    *
dtype0
\
$Init_optimizer_dense_19_dense_bias-mFillShape_42	Const_122*
T0*

index_type0
o
optimizer_dense_19_dense_bias-m
VariableV2*
shape:*
shared_name *
dtype0*
	container 
�
&Assign_optimizer_dense_19_dense_bias-mAssignoptimizer_dense_19_dense_bias-m$Init_optimizer_dense_19_dense_bias-m*
use_locking(*
T0*
validate_shape(
?
Shape_43Shapedense_19_dense_bias*
T0*
out_type0
6
	Const_123Const*
valueB
 *    *
dtype0
\
$Init_optimizer_dense_19_dense_bias-vFillShape_43	Const_123*
T0*

index_type0
o
optimizer_dense_19_dense_bias-v
VariableV2*
shape:*
shared_name *
dtype0*
	container 
�
&Assign_optimizer_dense_19_dense_bias-vAssignoptimizer_dense_19_dense_bias-v$Init_optimizer_dense_19_dense_bias-v*
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
6
	Const_124Const*
valueB
 *fff?*
dtype0
6
	Const_125Const*
valueB
 *w�?*
dtype0
6
	Const_126Const*
valueB
 *o�:*
dtype0
6
	Const_127Const*
valueB
 *���3*
dtype0
6
	Const_128Const*
valueB
 *��̽*
dtype0
6
	Const_129Const*
valueB
 *���=*
dtype0
[
ClipByValueClipByValue Gradients/Conv2DBackpropFilter_7	Const_128	Const_129*
T0
�
	ApplyAdam	ApplyAdamconv2d_2_conv2d_kernel"optimizer_conv2d_2_conv2d_kernel-m"optimizer_conv2d_2_conv2d_kernel-voptimizer_beta1_poweroptimizer_beta2_power	Const_126	Const_124	Const_125	Const_127ClipByValue*
use_locking(*
T0*
use_nesterov( 
6
	Const_130Const*
valueB
 *��̽*
dtype0
6
	Const_131Const*
valueB
 *���=*
dtype0
T
ClipByValue_1ClipByValueGradients/BiasAddGrad_7	Const_130	Const_131*
T0
�
ApplyAdam_1	ApplyAdamconv2d_2_conv2d_bias optimizer_conv2d_2_conv2d_bias-m optimizer_conv2d_2_conv2d_bias-voptimizer_beta1_poweroptimizer_beta2_power	Const_126	Const_124	Const_125	Const_127ClipByValue_1*
use_locking(*
T0*
use_nesterov( 
6
	Const_132Const*
valueB
 *��̽*
dtype0
6
	Const_133Const*
valueB
 *���=*
dtype0
]
ClipByValue_2ClipByValue Gradients/Conv2DBackpropFilter_6	Const_132	Const_133*
T0
�
ApplyAdam_2	ApplyAdamconv2d_3_conv2d_kernel"optimizer_conv2d_3_conv2d_kernel-m"optimizer_conv2d_3_conv2d_kernel-voptimizer_beta1_poweroptimizer_beta2_power	Const_126	Const_124	Const_125	Const_127ClipByValue_2*
use_locking(*
T0*
use_nesterov( 
6
	Const_134Const*
valueB
 *��̽*
dtype0
6
	Const_135Const*
valueB
 *���=*
dtype0
T
ClipByValue_3ClipByValueGradients/BiasAddGrad_6	Const_134	Const_135*
T0
�
ApplyAdam_3	ApplyAdamconv2d_3_conv2d_bias optimizer_conv2d_3_conv2d_bias-m optimizer_conv2d_3_conv2d_bias-voptimizer_beta1_poweroptimizer_beta2_power	Const_126	Const_124	Const_125	Const_127ClipByValue_3*
use_locking(*
T0*
use_nesterov( 
6
	Const_136Const*
valueB
 *��̽*
dtype0
6
	Const_137Const*
valueB
 *���=*
dtype0
]
ClipByValue_4ClipByValue Gradients/Conv2DBackpropFilter_5	Const_136	Const_137*
T0
�
ApplyAdam_4	ApplyAdamconv2d_5_conv2d_kernel"optimizer_conv2d_5_conv2d_kernel-m"optimizer_conv2d_5_conv2d_kernel-voptimizer_beta1_poweroptimizer_beta2_power	Const_126	Const_124	Const_125	Const_127ClipByValue_4*
use_locking(*
T0*
use_nesterov( 
6
	Const_138Const*
valueB
 *��̽*
dtype0
6
	Const_139Const*
valueB
 *���=*
dtype0
T
ClipByValue_5ClipByValueGradients/BiasAddGrad_5	Const_138	Const_139*
T0
�
ApplyAdam_5	ApplyAdamconv2d_5_conv2d_bias optimizer_conv2d_5_conv2d_bias-m optimizer_conv2d_5_conv2d_bias-voptimizer_beta1_poweroptimizer_beta2_power	Const_126	Const_124	Const_125	Const_127ClipByValue_5*
use_locking(*
T0*
use_nesterov( 
6
	Const_140Const*
valueB
 *��̽*
dtype0
6
	Const_141Const*
valueB
 *���=*
dtype0
]
ClipByValue_6ClipByValue Gradients/Conv2DBackpropFilter_4	Const_140	Const_141*
T0
�
ApplyAdam_6	ApplyAdamconv2d_6_conv2d_kernel"optimizer_conv2d_6_conv2d_kernel-m"optimizer_conv2d_6_conv2d_kernel-voptimizer_beta1_poweroptimizer_beta2_power	Const_126	Const_124	Const_125	Const_127ClipByValue_6*
use_locking(*
T0*
use_nesterov( 
6
	Const_142Const*
valueB
 *��̽*
dtype0
6
	Const_143Const*
valueB
 *���=*
dtype0
T
ClipByValue_7ClipByValueGradients/BiasAddGrad_4	Const_142	Const_143*
T0
�
ApplyAdam_7	ApplyAdamconv2d_6_conv2d_bias optimizer_conv2d_6_conv2d_bias-m optimizer_conv2d_6_conv2d_bias-voptimizer_beta1_poweroptimizer_beta2_power	Const_126	Const_124	Const_125	Const_127ClipByValue_7*
use_locking(*
T0*
use_nesterov( 
6
	Const_144Const*
valueB
 *��̽*
dtype0
6
	Const_145Const*
valueB
 *���=*
dtype0
]
ClipByValue_8ClipByValue Gradients/Conv2DBackpropFilter_3	Const_144	Const_145*
T0
�
ApplyAdam_8	ApplyAdamconv2d_8_conv2d_kernel"optimizer_conv2d_8_conv2d_kernel-m"optimizer_conv2d_8_conv2d_kernel-voptimizer_beta1_poweroptimizer_beta2_power	Const_126	Const_124	Const_125	Const_127ClipByValue_8*
use_locking(*
T0*
use_nesterov( 
6
	Const_146Const*
valueB
 *��̽*
dtype0
6
	Const_147Const*
valueB
 *���=*
dtype0
T
ClipByValue_9ClipByValueGradients/BiasAddGrad_3	Const_146	Const_147*
T0
�
ApplyAdam_9	ApplyAdamconv2d_8_conv2d_bias optimizer_conv2d_8_conv2d_bias-m optimizer_conv2d_8_conv2d_bias-voptimizer_beta1_poweroptimizer_beta2_power	Const_126	Const_124	Const_125	Const_127ClipByValue_9*
use_locking(*
T0*
use_nesterov( 
6
	Const_148Const*
valueB
 *��̽*
dtype0
6
	Const_149Const*
valueB
 *���=*
dtype0
^
ClipByValue_10ClipByValue Gradients/Conv2DBackpropFilter_2	Const_148	Const_149*
T0
�
ApplyAdam_10	ApplyAdamconv2d_9_conv2d_kernel"optimizer_conv2d_9_conv2d_kernel-m"optimizer_conv2d_9_conv2d_kernel-voptimizer_beta1_poweroptimizer_beta2_power	Const_126	Const_124	Const_125	Const_127ClipByValue_10*
use_locking(*
T0*
use_nesterov( 
6
	Const_150Const*
valueB
 *��̽*
dtype0
6
	Const_151Const*
valueB
 *���=*
dtype0
U
ClipByValue_11ClipByValueGradients/BiasAddGrad_2	Const_150	Const_151*
T0
�
ApplyAdam_11	ApplyAdamconv2d_9_conv2d_bias optimizer_conv2d_9_conv2d_bias-m optimizer_conv2d_9_conv2d_bias-voptimizer_beta1_poweroptimizer_beta2_power	Const_126	Const_124	Const_125	Const_127ClipByValue_11*
use_locking(*
T0*
use_nesterov( 
6
	Const_152Const*
valueB
 *��̽*
dtype0
6
	Const_153Const*
valueB
 *���=*
dtype0
^
ClipByValue_12ClipByValue Gradients/Conv2DBackpropFilter_1	Const_152	Const_153*
T0
�
ApplyAdam_12	ApplyAdamconv2d_11_conv2d_kernel#optimizer_conv2d_11_conv2d_kernel-m#optimizer_conv2d_11_conv2d_kernel-voptimizer_beta1_poweroptimizer_beta2_power	Const_126	Const_124	Const_125	Const_127ClipByValue_12*
use_locking(*
T0*
use_nesterov( 
6
	Const_154Const*
valueB
 *��̽*
dtype0
6
	Const_155Const*
valueB
 *���=*
dtype0
U
ClipByValue_13ClipByValueGradients/BiasAddGrad_1	Const_154	Const_155*
T0
�
ApplyAdam_13	ApplyAdamconv2d_11_conv2d_bias!optimizer_conv2d_11_conv2d_bias-m!optimizer_conv2d_11_conv2d_bias-voptimizer_beta1_poweroptimizer_beta2_power	Const_126	Const_124	Const_125	Const_127ClipByValue_13*
use_locking(*
T0*
use_nesterov( 
6
	Const_156Const*
valueB
 *��̽*
dtype0
6
	Const_157Const*
valueB
 *���=*
dtype0
\
ClipByValue_14ClipByValueGradients/Conv2DBackpropFilter	Const_156	Const_157*
T0
�
ApplyAdam_14	ApplyAdamconv2d_12_conv2d_kernel#optimizer_conv2d_12_conv2d_kernel-m#optimizer_conv2d_12_conv2d_kernel-voptimizer_beta1_poweroptimizer_beta2_power	Const_126	Const_124	Const_125	Const_127ClipByValue_14*
use_locking(*
T0*
use_nesterov( 
6
	Const_158Const*
valueB
 *��̽*
dtype0
6
	Const_159Const*
valueB
 *���=*
dtype0
S
ClipByValue_15ClipByValueGradients/BiasAddGrad	Const_158	Const_159*
T0
�
ApplyAdam_15	ApplyAdamconv2d_12_conv2d_bias!optimizer_conv2d_12_conv2d_bias-m!optimizer_conv2d_12_conv2d_bias-voptimizer_beta1_poweroptimizer_beta2_power	Const_126	Const_124	Const_125	Const_127ClipByValue_15*
use_locking(*
T0*
use_nesterov( 
6
	Const_160Const*
valueB
 *��̽*
dtype0
6
	Const_161Const*
valueB
 *���=*
dtype0
P
ClipByValue_16ClipByValueGradients/MatMul_5	Const_160	Const_161*
T0
�
ApplyAdam_16	ApplyAdamdense_15_dense_kernel!optimizer_dense_15_dense_kernel-m!optimizer_dense_15_dense_kernel-voptimizer_beta1_poweroptimizer_beta2_power	Const_126	Const_124	Const_125	Const_127ClipByValue_16*
use_locking(*
T0*
use_nesterov( 
6
	Const_162Const*
valueB
 *��̽*
dtype0
6
	Const_163Const*
valueB
 *���=*
dtype0
R
ClipByValue_17ClipByValueGradients/Reshape_11	Const_162	Const_163*
T0
�
ApplyAdam_17	ApplyAdamdense_15_dense_biasoptimizer_dense_15_dense_bias-moptimizer_dense_15_dense_bias-voptimizer_beta1_poweroptimizer_beta2_power	Const_126	Const_124	Const_125	Const_127ClipByValue_17*
use_locking(*
T0*
use_nesterov( 
6
	Const_164Const*
valueB
 *��̽*
dtype0
6
	Const_165Const*
valueB
 *���=*
dtype0
P
ClipByValue_18ClipByValueGradients/MatMul_3	Const_164	Const_165*
T0
�
ApplyAdam_18	ApplyAdamdense_17_dense_kernel!optimizer_dense_17_dense_kernel-m!optimizer_dense_17_dense_kernel-voptimizer_beta1_poweroptimizer_beta2_power	Const_126	Const_124	Const_125	Const_127ClipByValue_18*
use_locking(*
T0*
use_nesterov( 
6
	Const_166Const*
valueB
 *��̽*
dtype0
6
	Const_167Const*
valueB
 *���=*
dtype0
Q
ClipByValue_19ClipByValueGradients/Reshape_9	Const_166	Const_167*
T0
�
ApplyAdam_19	ApplyAdamdense_17_dense_biasoptimizer_dense_17_dense_bias-moptimizer_dense_17_dense_bias-voptimizer_beta1_poweroptimizer_beta2_power	Const_126	Const_124	Const_125	Const_127ClipByValue_19*
use_locking(*
T0*
use_nesterov( 
6
	Const_168Const*
valueB
 *��̽*
dtype0
6
	Const_169Const*
valueB
 *���=*
dtype0
P
ClipByValue_20ClipByValueGradients/MatMul_1	Const_168	Const_169*
T0
�
ApplyAdam_20	ApplyAdamdense_19_dense_kernel!optimizer_dense_19_dense_kernel-m!optimizer_dense_19_dense_kernel-voptimizer_beta1_poweroptimizer_beta2_power	Const_126	Const_124	Const_125	Const_127ClipByValue_20*
use_locking(*
T0*
use_nesterov( 
6
	Const_170Const*
valueB
 *��̽*
dtype0
6
	Const_171Const*
valueB
 *���=*
dtype0
Q
ClipByValue_21ClipByValueGradients/Reshape_7	Const_170	Const_171*
T0
�
ApplyAdam_21	ApplyAdamdense_19_dense_biasoptimizer_dense_19_dense_bias-moptimizer_dense_19_dense_bias-voptimizer_beta1_poweroptimizer_beta2_power	Const_126	Const_124	Const_125	Const_127ClipByValue_21*
use_locking(*
T0*
use_nesterov( 
5
MulMuloptimizer_beta1_power	Const_124*
T0
^
AssignAssignoptimizer_beta1_powerMul*
use_locking(*
T0*
validate_shape(
7
Mul_1Muloptimizer_beta2_power	Const_125*
T0
b
Assign_1Assignoptimizer_beta2_powerMul_1*
use_locking(*
T0*
validate_shape(
8
default_outputIdentityActivation_dense_19*
T0
3
	Const_172Const*
value	B :*
dtype0
S
ArgMaxArgMaxdefault_output	Const_172*

Tidx0*
T0*
output_type0	
3
	Const_173Const*
value	B :*
dtype0
R
ArgMax_1ArgMaxPlaceholder	Const_173*

Tidx0*
T0*
output_type0	
I
EqualEqualArgMaxArgMax_1*
incompatible_shape_error(*
T0	
>
Cast_22CastEqual*

SrcT0
*
Truncate( *

DstT0
3
	Const_174Const*
value	B : *
dtype0
H
Mean_1MeanCast_22	Const_174*

Tidx0*
	keep_dims( *
T0 "�