<template>
    <div>
        <div id="addTask" class="row">
            <div class="col-sm-12">
                <div class="form">
                    <div>
                        <!--suppress HtmlFormInputWithoutLabel -->
                        <input type="text" class="form-control" v-model="newTaskName" placeholder="Add task..."/>
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
                <tr v-for="task in tasks" :key="task.id">
                    <td v-bind:class="{ 'table-success': task.id === selectedTask.id }"
                        v-on:click="showTask(task)">
                        <span>{{task.name}}</span>
                        <span class="float-right">{{task.completeDate}}</span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</template>

<script>
    import {AXIOS} from "@/http-common";
    import {createUuid} from "@/main";

    export default {
        name: 'task-list',
        props: {
            listCode: String
        },
        data() {
            return {
                tasks: [],
                newTaskName: '',
                selectedTask: {}
            };
        },
        watch: {
            listCode: function(newValue, oldValue) {
                if (newValue !== undefined && newValue !== oldValue) {
                    AXIOS.get('/task/list', {
                        params: {
                            code: newValue
                        }
                    })
                        .then(response => this.tasks = response.data);
                    this.sendIsTaskSelectedChanged(false);
                }
            }
        },
        methods: {
            addTask: function (taskName) {
                let task = {id: createUuid(), createdAt: new Date(), name: taskName};
                if (this.selectedListCode !== 'Inbox') {
                    task.contexts = [this.selectedListCode];
                    task.status = 'Active';
                }
                AXIOS.post('/task/add', task)
                    .then(response => this.tasks.push(response.data));
                this.newTaskName = '';
            },
            showTask: function (task) {
                this.selectedTask = task;
                this.sendTaskSelected(task);
            },
            onTaskEditCompleted: function(task) {
                this.saveTask(task);
                this.sendTaskSelected(null);
            },
            sendTaskSelected: function(task) {
                this.$emit('task-selected', task);
            },
            saveTask: function (task) {
                AXIOS.post('/task/update', task)
                    .then(response => {
                        this.updateTask(response.data);
                        this.showTasks(this.selectedListCode);
                    });
            },
            updateTask: function (task) {
                const idx = this.tasks.findIndex(it => it.id === task.id);
                this.tasks[idx] = task;
            }
        }
    };
</script>
