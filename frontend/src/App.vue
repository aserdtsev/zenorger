<template>
  <div id="app" class="container" v-on:task-edit-completed="onTaskEditCompleted($event)">
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
      <div id="taskList" class="col-sm-5">
        <div id="addTask" class="row">
          <div class="col-sm-12">
            <div class="form">
              <div>
                <label>
                  <input type="text" class="form-control" v-model="newTaskName" placeholder="Add task..."/>
                </label>
              </div>
              <button class="btn btn-primary form-group"
                      v-bind:disabled="!newTaskName"
                      v-on:click="addTask(newTaskName)">Add task
              </button>
            </div>
          </div>
        </div>
        <div class="block">
          <table class="table table-sm table-hover">
            <tbody>
              <tr v-for="(task, i) in tasks" :key="i">
                <td v-bind:class="{ 'table-success': task.id === editableTask.id }"
                    v-on:click="showTask(task)">
                  <span>{{task.name}}</span>
                  <span class="float-right">{{task.completeDate}}</span>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
      <div id="taskForm" class="col-sm-5">
        <TaskEdit v-if="isTaskEdit"
                  editableTask="editableTask"
                  tasks="tasks"
                  statuses="statuses"
                  contexts="contexts"
                  v-on:clear-edit-task="this.clearEditableTask()"/>
      </div>
    </div>
    <HelloWorld msg="Welcome to Your Vue.js App"/>
  </div>
</template>

<script>
    import 'jquery'
    import 'popper.js'
    import 'bootstrap'
    import TaskEdit from './components/TaskEdit.vue'
    import HelloWorld from './components/HelloWorld.vue'
    import {AXIOS} from './http-common'
    import {createUuid, jsonCopy} from "@/main";

    export default {
        name: 'app',
        components: {
            TaskEdit,
            HelloWorld
        },
        data() {
            return {
                statuses: ['Inbox', 'Active', 'Pending', 'SomedayMaybe'],
                contexts: [],
                selectedListCode: 'Inbox',
                tasks: [],
                newTaskName: '',
                isTaskEdit: false,
                editableTask: {}
            };
        },
        methods: {
            showTasks: function (code) {
                this.selectedListCode = code;
                AXIOS.get('/task/list', {
                    params: {
                        code: code
                    }
                })
                    .then(response => this.tasks = response.data);
                this.isTaskEdit = false;
                this.$emit("clear-task-edit");
            },
            addTask: function (taskName) {
                let task = {id: createUuid(), createdAt: new Date(), name: taskName};
                if (this.selectedListCode !== 'Inbox') {
                    task.contexts = [this.selectedListCode];
                    task.status = 'Active';
                }
                AXIOS.post('/task/add', task)
                    .then(response => this.pushTask(response.data));
                this.newTaskName = '';
            },
            pushTask: function (task) {
                this.tasks.push(task);
                this.editableTask = task;
            },
            showTask: function (task) {
                this.editableTask = jsonCopy(task);
                this.isTaskEdit = true;
            },
            onTaskEditCompleted: function(task) {
                this.saveTask(task);
                this.isTaskEdit = false;
            },
            saveTask: function (task) {
                AXIOS.post('/task/update', task)
                    .then(response => {
                        this.updateTask(response.data);
                        this.showTasks(this.selectedListCode);
                    });
            },
            updateTask: function (task) {
                this.editableTask = task;
                const idx = this.tasks.findIndex(it => it.id === task.id);
                this.tasks[idx] = task;
            }
        },
        mounted() {
            AXIOS.get('/context/list')
                .then(response => (this.contexts = response.data));
            this.showTasks('Inbox');
        }
    }
</script>

<style>
  #app {
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    color: #2c3e50;
    margin-top: 60px;
  }

  [class*="col-"] {
    background-color: #eee;
    border-right: 2px solid #fff;
    border-bottom: 2px solid #fff;
  }

  .form {
    padding-top: 8px;
    padding-bottom: 8px;
  }

  .form-group {
    margin-top: 2px;
  }

  .form-control {
    margin-top: 2px;
  }
</style>
