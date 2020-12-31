//读取参数
let args = arguments
let x = args[0]
let y = args[1]
let w = args[2]
let h = args[3]

//创建元素
//let id = "tipNode"
//let divNode = document.getElementById(id);
//if (!divNode) {
let divNode = document.createElement("div");
//divNode.id = "tipNode"
divNode.style.position = 'absolute'
divNode.style.zIndex = 999999
divNode.classList.add("tipNode")
document.body.append(divNode)
//}

//更改元素位置
divNode.style.left = '0px'
divNode.style.top = '0px'
divNode.style.width = '100%'
divNode.style.height = '100%'

//样式
divNode.style.border = '0.2rem solid red'
divNode.style.pointerEvents = 'none'

//动画
divNode.style.transition = 'all 0.3s linear'

//执行动画
setTimeout(function () {
    divNode.style.left = x
    divNode.style.top = y
    divNode.style.width = w
    divNode.style.height = h
}, 160)
