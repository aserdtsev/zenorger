<template>
  <div id="app" class="container">
    <div class="row text-center">
      <div id="header" class="col-sm-12">Zenorger</div>
    </div>
    <div class="row">
      <navigate-bar class="col-sm-2"
                    v-bind:contexts="contexts"
                    v-bind:selectedListCode="selectedListCode"
                    v-on:list-selected="onListSelected($event)"/>
      <task-list class="col-sm-5" v-bind:list-code="selectedListCode"
                 v-bind:task-edit-completed="taskEditCompleted"
                 v-on:task-selected="onTaskSelected($event)"/>
      <div class="col-sm-5">
        <task-form v-if="selectedTask"
                   v-bind:initial-task="selectedTask"
                   v-bind:statuses="statuses"
                   v-bind:contexts="contexts"
                   v-on:task-edit-completed="onTaskEditCompleted($event)"/>
      </div>
    </div>
  </div>
</template>

<script>
    import 'jquery'
    import 'popper.js'
    import 'bootstrap'
    import TaskList from '@/components/TaskList'
    import TaskForm from '@/components/TaskForm'
    import NavigateBar from "@/components/NavigateBar";
    import {AXIOS} from '@/http-common'

    export default {
        name: 'app',
        components: {
            NavigateBar,
            TaskList,
            TaskForm
        },
        data() {
            return {
                statuses: ['Inbox', 'Active', 'Pending', 'SomedayMaybe'],
                contexts: [],
                selectedListCode: 'Inbox',
                selectedTask: null,
                taskEditCompleted: {}
            };
        },
        methods: {
            onListSelected: function (code) {
                this.selectedListCode = code;
            },
            onTaskSelected: function(task) {
                this.selectedTask = task;
            },
            onTaskEditCompleted: function(task) {
                this.taskEditCompleted = task;
                this.selectedTask = null;
            }
        },
        mounted() {
            AXIOS.get('/context/list')
                .then(response => (this.contexts = response.data));
        }
    }
</script>

<style scoped>
  @import 'assets/app.css';
</style>
