var vm = new Vue({
    el: '#app',
    data() {
        return {
            contexts: null,
            tasks: null
        };
    },
    methods: {
        showContextTasks: function(contextId) {
            this.tasks = [
                {
                    "id": "d5388c7b-89ee-40fb-8fed-83bf1ff71c92",
                    "name": "Task name",
                    "status": "Inbox",
                    "startDate": "2019-03-01",
                    "startTime": "09:01",
                    "completeDate": "2019-04-01",
                    "completeTime": "18:00",
                    "projectTasks": [],
                    "projects": [
                        {
                            "id": "e53e7a37-f7ec-4cc1-837d-d815a2ca2406",
                            "name": "Task name"
                        }
                    ],
                    "contexts": [],
                    "tags": [],
                    "comments": []
                },
                {
                    "id": "e53e7a37-f7ec-4cc1-837d-d815a2ca2406",
                    "name": "Task name",
                    "status": "Inbox",
                    "startDate": "2019-03-01",
                    "startTime": "09:01",
                    "completeDate": "2019-04-01",
                    "completeTime": "18:00",
                    "projectTasks": [
                        {
                            "id": "d5388c7b-89ee-40fb-8fed-83bf1ff71c92",
                            "name": "Task name"
                        }
                    ],
                    "projects": [],
                    "contexts": [
                        "bc5181fe-dbdc-4dbd-b422-290800adb828"
                    ],
                    "tags": [],
                    "comments": [],
                    "project": true
                },
                {
                    "id": "70457016-c00e-4f39-9461-16b849316cba",
                    "name": "Task name",
                    "status": "Inbox",
                    "startDate": "2019-03-01",
                    "startTime": "09:00",
                    "completeDate": "2019-04-01",
                    "completeTime": "18:00",
                    "projectTasks": [],
                    "projects": [],
                    "contexts": [],
                    "tags": [],
                    "comments": [],
                    "project": false
                }
            ]
            // axiosInst
            //     .get('/task/list')
            //     .then(response => (this.tasks = response.data));
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
                    'X-Organizer-Id': '59d27375-2dfe-4bec-8002-5e6514a7bce2',
                    'X-Request-Id': createUuid()
                }
            });
        axiosInst
            .get('/context/list')
            .then(response => (this.contexts = response.data));
        // axiosInst
        //     .get('/task/list')
        //     .then(response => (this.tasks = response.data));
    }
});

function createUuid() {
    return ([1e7]+-1e3+-4e3+-8e3+-1e11).replace(/[018]/g, c =>
        (c ^ crypto.getRandomValues(new Uint8Array(1))[0] & 15 >> c / 4).toString(16)
    )
}
