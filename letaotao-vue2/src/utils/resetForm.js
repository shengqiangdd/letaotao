//重置表单和表单数据
export default function resetForm(formName,obj) {
    //清空表单
    if(this.$refs[formName]) {
        this.$refs[formName].resetFields();
    }
    //清空数据域
    Object.keys(obj).forEach(key => {
        // 如果是数值类型，则清空为 0
        if (typeof obj[key] === 'number') {
            obj[key] = 0;
        } else {
            obj[key] = '';
        }
    })
}