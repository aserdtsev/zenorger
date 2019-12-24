var vm = new Vue({
    el: '#app',
    data() {
        return {
            contexts: null,
            selectedList: 'Inbox', /** 'Inbox' или contextId */
            tasks: null,
            newTaskName: '',
            currentTask: { },
        };
    },
    methods: {
        showInboxTasks: function() {
            this.selectedList = 'Inbox'
            axiosInst
                .get('/task/inbox')
                .then(response => (this.tasks = response.data));
        },
        showContextTasks: function(contextId) {
            this.selectedList = contextId
            axiosInst
                .get('/task/list', {
                    params: {
                        contextId: contextId
                    }
                })
                .then(response => (this.tasks = response.data));
        },
        addTask: function(taskName) {
            let task = { id: createUuid(), createdAt: new Date(), name: taskName };
            if (this.selectedList !== 'Inbox') {
                task.contexts = [this.selectedList]
                task.status = 'Active'
            }
            axiosInst
                .post('/task/add', task)
                .then(response => (this.pushTask(response.data)))
            this.newTaskName = ''
        },
        showTask: function(task) {
            this.currentTask = jsonCopy(task);
        },
        saveTask: function(task) {
            axiosInst
                .post('/task/update', task)
                .then(response => (this.updateTask(response.data)))
        },
        pushTask: function(task) {
            this.tasks.push(task);
            this.currentTask = task;
        },
        updateTask: function(task) {
            this.currentTask = task;
            let idx = this.tasks.findIndex(it => it.id === task.id);
            this.tasks[idx] = task
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
        this.showInboxTasks();
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
