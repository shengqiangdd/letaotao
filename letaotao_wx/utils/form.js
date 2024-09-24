// utils/validate.js
import toast from './toast'

export function validateField(field, value, rules) {
    // 如果该字段在rules中有对应的验证规则，则进行验证
    if (rules[field]) {
        if (!rules[field].validate(value)) {
            // 验证失败时，使用toast.show显示错误消息
            toast.show(rules[field].message);
            return false;
        }
    }
    return true;
}

export function isFormValid(formObj, rules) {
    for (const key in formObj) {
        if (Object.hasOwnProperty.call(formObj, key)) {
            const value = formObj[key];
            if (!validateField(key, value, rules)) {
                return false;
            }
        }
    }
    return true;
}

/**
 * 重置表单数据
 * @param {Object} formObj - 表单数据对象
 * @param {Object} [typeMap] - 属性类型映射表，指定每种类型的初始值
 * @returns {Object} - 重置后的表单数据对象
 */
export function resetForm(formObj, typeMap = {}) {
    for (const key in formObj) {
        if (Object.hasOwnProperty.call(formObj, key)) {
            const type = typeMap[key] || typeof formObj[key];
            switch (type) {
                case 'string':
                    formObj[key] = '';
                    break;
                case 'number':
                    formObj[key] = 0;
                    break;
                case 'boolean':
                    formObj[key] = false;
                    break;
                case 'object':
                    formObj[key] = null;
                    break;
                case 'array':
                    formObj[key] = [];
                    break;
                default:
                    formObj[key] = ''; // 默认为字符串
                    break;
            }
        }
    }
    return formObj;
}