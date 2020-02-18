<template>
  <div id="app" class="container">
    <div class="row text-center">
      <div id="header" class="col-sm-12">Zenorger</div>
    </div>
    <div class="row">
      <div class="col-sm-2">
        <button class="btn btn-link"
                v-bind:class="{ 'btn-outline-primary': selectedListCode === 'Inbox' }"
                v-on:click="showTasks('Inbox')">Inbox
        </button>
        <div>Contexts</div>
        <ul>
          <li v-for="context in contexts" :key="context.id">
            <button class="btn btn-link"
                    v-bind:class="{ 'btn-outline-primary': context.id === selectedListCode }"
                    v-on:click="showTasks(context.id)">{{context.name}}
            </button>
          </li>
        </ul>
        <button class="btn btn-link"
                v-bind:class="{ 'btn-outline-primary': selectedListCode === 'Pending' }"
                v-on:click="showTasks('Pending')">Pending
        </button>
        <button class="btn btn-link"
                v-bind:class="{ 'btn-outline-primary': selectedListCode === 'SomedayMaybe' }"
                v-on:click="showTasks('SomedayMaybe')">Someday / Maybe
        </button>
        <button class="btn btn-link"
                v-bind:class="{ 'btn-outline-primary': selectedListCode === 'Removed' }"
                v-on:click="showTasks('Removed')">Trash
        </button>
      </div>
      <div class="col-sm-5">
        <task-list v-bind:list-code="selectedListCode"
                   v-bind:task-edit-completed="taskEditCompleted"
                   v-on:task-selected="onTaskSelected($event)"/>
      </div>
      <div class="col-sm-5">
        <task-form v-if="selectedTask !== null"
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
    import {AXIOS} from '@/http-common'

    export default {
        name: 'app',
        components: {
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
            showTasks: function (code) {
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
            this.showTasks('Inbox');
        }
    }
</script>

<style scoped>
  @import 'assets/app.css';
</style>
