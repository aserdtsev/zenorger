var vm = new Vue({
    el: '#app',
    data() {
        return {
            statuses: ['Inbox', 'Active', 'Pending', 'SomedayMaybe'],
            contexts: null,
            selectedListCode: 'Inbox',
            tasks: null,
            newTaskName: '',
            editableTask: { },
            editableTaskLastContexts: [],
            isTaskEdit: false
        };
    },
    computed: {
        editableTaskStatus: function() {
            return this.editableTask.status;
        },
        editableTaskContexts: function() {
            return this.editableTask.contexts;
        }
    },
    watch: {
        editableTaskStatus: function(newStatus, oldStatus) {
            if (oldStatus === undefined || newStatus === oldStatus) return;
            if (newStatus === 'Active' && !this.editableTask.contexts.length) {
                this.editableTask.contexts = this.editableTaskLastContexts;
            } else if (newStatus !== 'Active' && this.editableTask.contexts.length) {
                this.editableTask.contexts = [];
            }
        },
        editableTaskContexts: function(newContexts, oldContexts) {
            if (oldContexts === undefined || JSON.stringify(newContexts) === JSON.stringify(oldContexts)) return;
            if (!newContexts.length && this.editableTask.status === 'Active') {
                this.editableTask.status = 'Inbox';
                this.editableTaskLastContexts = oldContexts;
            } else if (this.editableTask.status !== 'Active') {
                this.editableTask.status = 'Active';
            }
        }
    },
    methods: {
        showTasks: function(code) {
            this.selectedListCode = code;
            axiosInst
                .get('/task/list', {
                    params: {
                        code: code
                    }
                })
                .then(response => this.tasks = response.data);
            this.isTaskEdit = false;
        },
        addTask: function(taskName) {
            let task = { id: createUuid(), createdAt: new Date(), name: taskName };
            if (this.selectedListCode !== 'Inbox') {
                task.contexts = [this.selectedListCode];
                task.status = 'Active';
            }
            axiosInst
                .post('/task/add', task)
                .then(response => this.pushTask(response.data));
            this.newTaskName = ''
        },
        showTask: function(task) {
            this.editableTask = jsonCopy(task);
            this.isTaskEdit = true
        },
        saveTask: function(task) {
            axiosInst
                .post('/task/update', task)
                .then(response => {
                    this.updateTask(response.data);
                    this.showTasks(this.selectedListCode);
                });
            this.isTaskEdit = false;
        },
        pushTask: function(task) {
            this.tasks.push(task);
            this.editableTask = task;
        },
        updateTask: function(task) {
            this.editableTask = task;
            let idx = this.tasks.findIndex(it => it.id === task.id);
            this.tasks[idx] = task;
        },
        getContextName: function(contextId) {
            return this.contexts.find(item => item.id === contextId).name;
        }
    },
    mounted() {
        axiosInst = axios.create({
            baseURL: 'http://localhost:8080/api',
            withCredentials: true,
            auth: {
                username: 'andrey.serdtsev@gmail.com',
                password: '123456'
            },
            headers: {
                "Accept": "application/json",
                "Content-Type": "application/json",
                'X-Organizer-Id': '640021fc-4093-4dd4-84f2-5792a6116cb7',
                'X-Request-Id': createUuid()
            }
        });
        axiosInst
            .get('/context/list')
            .then(response => (this.contexts = response.data));
        this.showTasks('Inbox');
    }
});

function createUuid() {
    return ([1e7]+-1e3+-4e3+-8e3+-1e11).replace(/[018]/g, c =>
        (c ^ crypto.getRandomValues(new Uint8Array(1))[0] & 15 >> c / 4).toString(16)
    )
}

function jsonCopy(src) {
    return JSON.parse(JSON.stringify(src));
}
