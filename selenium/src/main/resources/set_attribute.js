/**设置元素属性*/

//读取参数
let args = arguments
let css = args[0] //目标元素
let attrKey = args[1] //属性名
let attrValue = args[2] //属性值

if (css) {
    let all = document.querySelectorAll(css);
    if (all) {
        all.forEach(item => {
            item.setAttribute(attrKey, attrValue)
        })
    }
}

