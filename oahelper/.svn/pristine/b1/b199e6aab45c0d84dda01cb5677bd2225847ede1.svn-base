<style lang="less">
    @import './styles/to-do-list-item.less';
</style>

<template>
    <Row class="to-do-list-item">
        <Col span="2" class="height-100">
            <Row type="flex" justify="center" align="middle" class="height-100">
                <!--<Checkbox v-model="todoitem"></Checkbox>-->
                <Icon type="android-checkbox-outline-blank" size="20" color="#F0F0F0"></Icon>
            </Row>
        </Col>
        <Col span="22" class="height-200">
            <Row type="flex" justify="start" align="middle" class="height-100">
                <p class="to-do-list-item-text" @click="handleHasDid" >{{ title }}</p>
            </Row>
            <Row type="flex" justify="start" align="middle" class="height-100">
                <p class="to-do-list-item-text" @click="handleHasDid" >{{ content }}</p>
            </Row>
        </Col>
    </Row>
</template>

<script>
export default {
    name: 'toDoListItem',
    data () {
        return {
            todoitem: false
        };
    },
    props: {
        title: String,
        content: String
    },
    methods: {
        handleHasDid () {
//            this.todoitem = !this.todoitem;
        }
    }
};
</script>