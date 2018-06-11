---
typora-root-url: ..\..
---

# Artificial Neural Network

인공신경망은 뉴런의 시냅스 결합으로 네트워크를 형성한 인공 뉴런(노드)이 학습을 통해 시냅스 결합 세기를 변화시켜, 문제 해결 능력을 가지는 모델 전반을 가리킨다.  

인공신경망에는 지도(정답)의 입력에 의해서 문제에 최적화되어 가는 Supervised learning과 지도를 필요로 하지 않는 Unsupervised learning이 있다. 명확한 해답이 있는 경우에는 supervised learning이, 데이터 클러스터링에는 unsupervised learning이 이용된다.  

'인공신경망'에서 *망*은 각 시스템에 있는 여러 층의 뉴런 간의 연결을 의미한다. 예를 들어 세 층이 있는 시스템이 있다면, 첫 번째 층은 시냅스를 통해 두 번째 층의 뉴런들로 데이터를 보내는 입력 뉴런들이 있고, 더 많은 시냅스를 통해 세 번째 층의 출력 뉴런으로 신호를 보내는 식이다.  

인공신경망은 보통 세 가지의 인자를 이용해 정의된다.

1. 다른 층의 뉴런들 사이의 연결 패턴
2. 연결의 가중치를 갱신하는 학습 과정
3. 뉴런의 가중 입력을 활성화도 출력으로 바꿔주는 활성화 함수





## Components of an artificial neural network

### Neuron(Perceptron)

![Perceptron](/study/ANN/Perceptron.jpg)



- $X_i(t)$ 는 input vector의 $i$th component. 

- $w_{ij}$ 는 $X_i$의 weight.

- $\theta_j$는 뉴런 $j$의 threshold.

- $p_j(t)$ 는 뉴런 $j$로 들어오는 input vector의 weighted sum.
  $$
  p_j(t) = \sum_{i} X_i(t)*w_{ij}
  $$






- $f$ 는 activation function

- $o_j(t)$ 는 뉴런 $j$의 output.
  $$
  o_j = f(p_j, \theta_j)\\
  o_j = f(\sum_ip_j(t)-\theta_j)
  $$






### Network

 network는 connection들로 구성되어 있으며, 각각의 connection은 뉴런  ![i](https://wikimedia.org/api/rest_v1/media/math/render/svg/add78d8608ad86e54951b8c8bd6c8d8416533d20) 의 output 을 뉴런 ![j](https://wikimedia.org/api/rest_v1/media/math/render/svg/2f461e54f5c093e92a55547b9764291390f0b5d0) 의 input으로 연결한다. 이 때, ![i](https://wikimedia.org/api/rest_v1/media/math/render/svg/add78d8608ad86e54951b8c8bd6c8d8416533d20) 는  ![j](https://wikimedia.org/api/rest_v1/media/math/render/svg/2f461e54f5c093e92a55547b9764291390f0b5d0) 의 predecessor이고,  ![j](https://wikimedia.org/api/rest_v1/media/math/render/svg/2f461e54f5c093e92a55547b9764291390f0b5d0) 는 ![i](https://wikimedia.org/api/rest_v1/media/math/render/svg/add78d8608ad86e54951b8c8bd6c8d8416533d20) 의 successor이다. 각각의 connection은 weight ![w_{ij}](https://wikimedia.org/api/rest_v1/media/math/render/svg/d3302ff355269436b43bc2fbe180303881c09321)  로 assign 되어 있다.



### Learning rule

Learning rule은 원하는 결과값을 얻기위해 neural network의 parameter들을 수정하는 규칙, 알고리즘이다. 이 과정에서 weight들이나 threshold 값들이 변경된다.



### Loss function(Error function)

 Loss function(Error function)은 neural network의 output과 expected output 간의 차이를 계산한다.
$$
E_p = \frac{1}{2}\sum_j(d_j-y_j)^2
$$

### Chain rule

![Chain rule](/study/ANN/chain rule.png) 

 각 parameter 별로 Loss function에 대한 gradient를 구할 때, 미분의 chain rule에 의해서 다음과 같이 계산할 수 있다. 
$$
{\partial{L}\over\partial{x}} = {\partial{y}\over\partial{x}}{\partial{L}\over\partial{y}}
$$
${\partial{L}\over\partial{x}}$ 는 Loss로 부터 흘러들어온 gradient이고, ${\partial{y}\over\partial{x}}$ 는 현재 입력 값에 대한 현재 연산결과의 변화량, 즉, Local gradient이다. 즉, 현재 입력 $x$ 에 대한 Loss 의 변화량은 흘러들어온 gradient 에 local gradient를 곱해서 구한다는 것이다.



## Delta rule

 Delta rule은 single layer ANN을 통과한 결과와 실제 목표한 값의 차이를 최소화 하기 위한 **learning rule**로, input들의 weight들을 gradient descent (경사하강법)으로 오차를 최소화한다. 



### Learning

Error function 을 뉴런 $j$의 각각의 weight에 대하여 편미분하여 gradient descent rule을 적용한다. 다음 식은 뉴런 $j$ 의 $i$th weight $w_{ij}$ 에 대하여 편미분하여 $w_{ij}$ 에 대한 gradient를 구하여, gradient descent rule 을 적용한 식이다.


$$
{\partial{E_p}\over\partial{w_{ij}}} = {\partial{E_p}\over\partial{y_j}}{\partial{y_j}\over\partial{h_j}}{\partial{h_j}\over\partial{w_{ij}}}  \  \ \ \ \ \ \because \mbox{chain rule}\\
={\partial{(\frac{1}{2}(d_j - y_j)^2)}\over\partial{y_j}}{\partial{y_j}\over\partial{h_j}}{\partial{h_j}\over\partial{w_{ij}}} \ \ \ \ \ \  \because \mbox{single layered ANN } \\
=-(d_j-y_j)f'(h_j){\partial{h_j}\over\partial{w_{ij}}} \ \ \ \ \ \ \ \because y_j = f(h_j)\\
=-(d_j-y_j)f'(h_j)X_i
$$

$$
\Delta w_{ij}(t) =  \alpha(d_j(t) - y_j(t))*f'(h_j(t))*X_i(t)
$$

$$
\alpha : learning \ rate ,  \ d_j : target \ output, \ y_j:actual\ output\\f:activation\ function ,\  h_j:weighted \ sum\ of \ neuron's \ input\\x_i : ith \ input
$$

  Linear activation function 형태의 간단한 neuron일 경우, delta rule은 아래와 같다.
$$
\Delta w_{ij}(t) =\alpha(d_j(t)-y_j(t))*x_i(t)\\
w_{ij}(t+1) = w_{ij}(t) + \Delta w_{ij}(t)
$$




## Backpropagation Neural Network (Multi-layered Perceptron)

 일반적인 perceptron 하나는 input layer와 output layer로 구성되어 있는 single layered ANN 이다. 하지만 이런 단일 layer ANN 은 단순한 XOR 문제와 같은 linearly non-sperable 한 문제를 해결하지 못한다. 이러한 문제를 해결하기 위해서 Multi-layered perceptron을 사용하여 Backpropagation Neural Network 가 사용된다. 



### Learning

 Error back propagtion algorithm을 바탕으로 진행되며, 이는 delta rule의 일반화된 규칙이다. 

 Delta rule에서는 output layer에서 발생한 error를 이용하여 input layer의 weight들을 갱신하였었다. Error backpropagation은 multi-layered 구조이므로, 이 과정에 hidden layer 의 weight 갱신과정이 추가된다. 즉,

- Output layer에서 발생한 error를 통해 hidden layer의 weight를 갱신

- 갱신된 값을 input layer로 backpropagate 시켜 input layer의 weight 갱신

- Weight 갱신은 gradient descent rule로 error를 최소화 하는 방식

  

![Error backpropagation](/study/ANN/Error backpropagation.png)

1. 모든 가중치 **$W$**와 임계치 $\theta$ 를 임의의 값으로 초기화 시킨다.

2. 입력 $X_p$ 와 목표 출력 $d_p$ 를 제시한다. 

3. 제시된 입력을 이용하여 Hidden layer의 $j$번째 뉴런으로의 입력은 다음과 같다.
   $$
   Input_{pj} = \sum_{i}X_{pi}*W_{ij}-\theta_{j}
   $$

4. Activation function을 사용하여 Hidden layer의 출력 $O_{pj}$ 를 계산한다.
   $$
   O_{pj} = f(Input_{pj})
   $$

5. Hidden layer의 출력을 이용하여 Output layer $k$번째 뉴런으로의 입력은 다음과 같다.
   $$
   Input_{pk} = \sum_{j}O_{pj}*W_{jk}-\theta_{k}
   $$

6. Activation function을 사용하여 Output layer의 출력 $O_{pk}​$를 계산한다.
   $$
   O_{pk} = f(Input_{pk})
   $$

7. 출력 $O_{pk}$와 목표 출력 $d_{pk}$ 값을 비교하여 $Error \ function= E_p$  를 구한다.
   $$
   E_p = \frac{1}{2}\sum_{k}(d_{pk}-O_{pk})^2
   $$

8. Neural network의 weight에 대한 gradient를 구한다. (각 가중치 $W$에 대한 $E_p$의 변화율)

    

   ![backpropagation_step1](/study/ANN/backpropagation_step1.png)

   Hidden layer의 $j$ 뉴런에서 output layer $k$ 뉴런을 연결하는 weight $W_{jk}$ 에 대한 $E_p$의 변화율은 다음과 같다.
   $$
   -{\partial{E_p}\over\partial{W_{jk}}} = {\partial{E_p}\over\partial{O_{pk}}}{\partial{O_{pk}}\over\partial({Input_{pk})}}{\partial({Input_{pk}})\over\partial{W_{jk}}} \ \ \ \ \ \because \mbox{chain rule}
   $$
   $k$ 는 $W_{jk}$의 하나의 $k$만이 relate 되어 있으므로(하나의 $k$만 error에 영향), 
   $$
   -{\partial{E_p}\over\partial{W_{jk}}} = (d_{pk}-O_{pk})f'(Input_{pk})O_{pj}
   $$
   이고, activation function이 $sigmoid function$일 경우,
   $$
   y = f(x) = \frac{1}{1+e^{-x}}  \ \  \ \therefore {{\partial{y}}\over\partial{x}} = y(1-y)
   $$

   $$
   -{\partial{E_p}\over\partial{W_{jk}}} = (d_{pk}-O_{pk})O_{pk}(1-O_{pk})O_{pj}\\=\delta_{pk}*O_{pj} \\(\because \delta_{pk} = (d_{pk}-O_{pk})O_{pk}(1-O_{pk}))
   $$

   ${\partial{E_p}\over\partial{W_{ij}}}$ 는 $W_{ij}$ 가 최종 $Loss function$에 얼마나 영향을 주는가를 나타내는 값이다. 

    

   ![backpropagation_step2](/study/ANN/backpropagation_step2.png)

    그림에서, $W_{ij}$는 $Loss \ function$ $E_p$에 모든 $k$에 대하여 영향을 준다. (relate 되어 있다). 따라서, input layer의 $i$ 뉴런에서 hidden layer $j$ 뉴런을 연결하는 weight $W_{ij}$ 에 대한 $E_p$의 변화율은 다음과 같다.
   $$
   -{\partial{E_p}\over\partial{W_{ij}}} = -{\partial{E_p}\over{\partial{O_{pk}}}}{\partial{O_{pk}}\over\partial{(Input_{pk})}}{\partial{(Input_{pk})}\over\partial{O_{pj}}}{\partial{O_{pj}}\over\partial{(Input_{pj})}}{\partial{(Input_{pj})}\over\partial{W_{ij}}}\\
      =\sum_{k}(d_{pk}-O_{pk}) f'(Input_{pk})W_{jk}f'(Input_{pj})X_{pi}\\
      =\sum_{k}\delta_{pk}*W_{jk}*f'(Input_{pj})*X_{pi}
   $$
    $\partial{E_p}\over\partial{W_{ij}}$는 이전 단계의 backpropagation $\delta_{pk}$ 에 weight를 곱하고, 자신의 derivated activation function에 자신의 입력을 곱한 값이된다.

   

   $activation \ function$ 이 sigmoid 함수일 경우, $\partial{E_p}\over\partial{W_{ij}}$ 는 다음과 같다.
   $$
   {\partial{E_p}\over\partial{W_{ij}}}= \sum_k(d_{pk} - O_{pk})O_{pk}(1-O_{pk})W_{jk}*O_{pj}(1-O_{pj})X_{pi}\\
      = \sum_k\delta_{pk}W_{jk}*O_{pj}(1-O_{pj})X_{pi}\\
      = \delta_{pj}*X_{pi}\\
      \left(\because \ \delta_{pj} = \sum_k\delta_{pk}W_{jk}*O_{pj}(1-O_{pj})\right)
   $$
    $\delta$ 가 의미하는 바는 현재 단계의 backpropagate error이고, 이전단계의 backpropagate error * 해당 weight * 현재 뉴런의 activation function을 미분한 함수 이다)


$$
   \delta_{j} = \sum\delta_{k}*W_{jk}*f'_j(input_j)
$$

9. 앞서 구한 weight에 대한 gradient를 통해 weight를 갱신한다.


$$
   W_{jk}(t+1) = W_{jk}(t) + \alpha*\delta_{pk}*O_{pj}\\
   W_{ij}(t+1) = W_{ij}(t) + \alpha*\delta_{pj}*X_{pi}
$$

10. 모든 학습쌍에 대하여 전부 학습할 때 까지 2로 분기하여 반복 수행한다.

11. 출력층의 $E_p$가 허용값 이하이거나 최대 반복횟수보다 크면 종료, 아니면 2로 분기하여 반복수행한다.





## Hopfield memory

 Hopfield memory는 자신을 제외한 모든 뉴런과 양방향으로 상호 연결된 형태의 ANN 이다. 하나의 뉴런층에서 상호연결된 형태로, 입력벡터와 출력벡터의 차원이 동일하다. (입력과 출력이 같은 층에서 이루어 진다.)  다른 ANN과는 달리 초기 학습패턴들으로 고정된 weight값을 만들고 입력 패턴이 들어올 때 마다 이를 이용하여 학습한 패턴 정보를 연상한다.

 Hopfield memory는 network의 크기에 따라서 학습되는 패턴 수가 제한되어 있다. 보통 뉴런의 수가 N개 일 때, 0.15N개의 패턴을 기억할 수 있다. 수렴된 결과가 최적인지 보장을 할 수 없다는 문제점도 갖고 있다.

 



### Learning

 기본 학습 패턴은 양극화 모델 bipolar(+1, -1)을 사용하며, activation function으로 hard limiter를 사용한다. 즉, 학습패턴의 입력은 +1 또는 -1로 이루어져있다. (색칠부분, 흰부분으로 생각하면 되겠다.)



1. M개의 초기 학습패턴을 이용하여 N개 뉴런의 weight를 설정한다. (weight matrix로 표현)


$$
   W_{ij} = {\begin{cases}\sum_{s=0}^{M-1}X_{i} ^{\ S}X_j^{\ S} &{if \  \  (i\neq j)}\\  \\ 0 & if\ (i=j)\end{cases}}\\  
   X_{i}^{\ S} \mbox{는 }s\mbox{번째 }input \ vector\mbox{의 }i\mbox{th component 를 나타낸다}
$$

2. 입력 패턴을 Hopfield memory 에 제시한다.


$$
   \mu_i (0) = x_i \ \ \ \ \ \ \ \ \  0\leq i \leq N-1
$$

3. 뉴런들의 출력과 가중치를 곱한 값을 합하여 Activation function에 통과시킨다. (입력 패턴 행렬을 Weight 행렬과 곱한 결과를 activation function에 통과시킨 결과가 출력패턴)
   $$
   \mu_j(t+1) = f_b(\sum_{i=0}^{N-1}W_{ij}*\mu_i(t)) \ \ \ \ \ \ \ \ 0 \leq j \leq N-1
   $$

4. 뉴런의 출력 ($\mu_i$) 가 변화가 없을 때 까지 3을 반복한다.





## Self Organizing Map(SOM)

 SOM이란 고차원 데이터의 각 개체들이 저차원(2,3 차원) 격자에 대응하도록 ANN 과 유사한 방식의 학습을 통해 clustering 하는 기법이다. 비슷한 위치의 뉴런은 비슷한 인지기능을 수행한다고 가정하여, 데이터가 유사할 수록 인접하거나 같은 저차원 격자에 연결된다.

![self_organized_map](/study/ANN/self_organized_map.png)



 위 그림에서 input vector를 입력하면, output layer의 뉴런이 각 뉴런의 weight 에 따라 활성화가 되는데 가장 활성화 값이 크게 나오는 뉴런이 승자뉴런(Winner neuron) 이 된다. input vector는 가장 활성화가 크게 된 뉴런에 속하게 된다. (clustering)



 그렇다면 임의의 input vector가 주어졌을 때, 가장 활성화가 크게 된 뉴런(승자뉴런)을 어떻게 구할 수 있을까? 활성화 뉴런은 input vector를 weighted sum 한 결과를 값으로 갖게 되는데, 이는 input vector와 weight vector를 내적한 결과와 같다. 벡터의 내적 값은 두 벡터가 가까울 수록(비슷할 수록) 그 값이 커지므로, input vector와 distance가 가장 작은 weight vector를 갖는 뉴런이 활성화가 가장 클 것이다. 따라서 다음과 같이 승자뉴런을 구할 수 있다.


$$
d_j=\sum_{i}(X_i(t) - W_{ij}(t))^2\\
\mbox{승자뉴런은 가장 작은 }d_j\mbox{를 갖는 }j\\
X_i\mbox{는 } input\ vector\mbox{의 }i\mbox{번째 } component
\\
W_{ij}\mbox{는 } i\mbox{번째 } component \mbox{에서 } j\mbox{ 뉴런으로 연결되는 } weight
$$


 SOM은 여기서 추가적으로 다른 일반적인 learning과 달리 승자뉴런만 학습하는 것이 아니라, 승자뉴런과 인접한 뉴런들 역시 학습시킨다.



### Learning

1. 모든 weight($W$)들을 초기화 한다.

2. 새로운 입력패턴 벡터를 입력뉴런에 제시한다.

3. 입력패턴 벡터와 모든 출력 뉴런의 weight 벡터와의 거리를 계산한다.

  
   $$
   d_j = \sum_i(X_i(t)-W_{ij}(t))^2 \ \ \  \ \ \ \ \ (j\mbox{는 출력 뉴런의 }index)
   $$

4. 최소 거리를 가지는 출력 뉴런이 가장 활성화 되는 승자 뉴런($j^*$)

5. 승자 뉴런($j^*$)와 이웃 반경내의 뉴런들을 갱신한다.

  
   $$
   W_{ij}(t+1) = W_{ij}(t) + \alpha(X_i(t)-W_{ij}(t))\ \  \ \ \ \  (j\mbox{는 }j* \mbox{와 인접한 모든 뉴런})
   $$

6. 모든 입력패턴 벡터를 처리할 때 까지 2부터 다시 반복한다.

7. 이웃 반경을 감소시키면서 2~6 과정을 충분히 반복 학습시킨다.