import axios from 'axios'

export const AXIOS = axios.create({
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

export function createUuid() {
    return ([1e7]+-1e3+-4e3+-8e3+-1e11).replace(/[018]/g, c =>
        (c ^ crypto.getRandomValues(new Uint8Array(1))[0] & 15 >> c / 4).toString(16)
    )
}
