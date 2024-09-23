/**
 * Check if an element has a class
 * @param {HTMLElement} ele
 * @param {string} cls
 * @returns {boolean}
 */
export function hasClass(ele: HTMLElement, cls: string) {
  return !!ele.className.match(new RegExp("(\\s|^)" + cls + "(\\s|$)"));
}

/**
 * Add class to element
 * @param {HTMLElement} ele
 * @param {string} cls
 */
export function addClass(ele: HTMLElement, cls: string) {
  if (!hasClass(ele, cls)) ele.className += " " + cls;
}

/**
 * Remove class from element
 * @param {HTMLElement} ele
 * @param {string} cls
 */
export function removeClass(ele: HTMLElement, cls: string) {
  if (hasClass(ele, cls)) {
    const reg = new RegExp("(\\s|^)" + cls + "(\\s|$)");
    ele.className = ele.className.replace(reg, " ");
  }
}

/**
 * 判断是否是外部链接
 *
 * @param {string} path
 * @returns {Boolean}
 */
export function isExternal(path: string) {
  const isExternal = /^(https?:|http?:|mailto:|tel:)/.test(path);
  return isExternal;
}

//快速把obj1里面对应的数据复制到obj2
export async function objCopy(obj1: any, obj2: any) {
  for (const key in obj1) {
    if (obj1.hasOwnProperty(key) && obj2.hasOwnProperty(key)) {
      obj2[key] = obj1[key];
    }
  }
}

import { ElMessageBox } from "element-plus";

export function confirm(text: string) {
  return new Promise((resolve, reject) => {
    ElMessageBox.confirm(text, "系统提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    })
      .then(() => {
        resolve(true);
      })
      .catch(() => {
        resolve(false);
      });
  });
}

//重置表单和表单数据
export function resetForm(formRef: any, obj: any) {
  //清空表单
  if (formRef) {
    formRef.resetFields();
  }
  //清空数据域
  Object.keys(obj).forEach((key) => {
    // 如果是数值类型，则清空为 0
    switch (typeof obj[key]) {
      case "number":
        obj[key] = 0;
        break;
      case "string":
        obj[key] = "";
        break;
      case "boolean":
        obj[key] = false;
        break;
      case "object":
        // 检查是否为数组类型
        if (Array.isArray(obj[key])) {
          obj[key] = [];
        } else {
          obj[key] = null; // 对于非数组的对象类型
        }
        break;
      default:
        obj[key] = "";
    }
  });
}

//判断菜单节点是否是末级节点
export function leafUtils() {
  const setLeaf = (arr: any[]) => {
    if (arr && arr.length > 0) {
      for (let i = 0; i < arr.length; i++) {
        if (arr[i].children && arr[i].children.length > 0) {
          arr[i].open = false;
          setLeaf(arr[i].children);
        } else {
          arr[i].open = true;
        }
      }
    }
    return arr;
  };
  return {
    setLeaf,
  };
}
