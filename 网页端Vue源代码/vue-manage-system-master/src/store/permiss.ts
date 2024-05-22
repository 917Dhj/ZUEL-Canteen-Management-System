import { defineStore } from 'pinia';

interface ObjectList {
    [key: string]: string[];
}

export const usePermissStore = defineStore('permiss', {
    state: () => {
        const defaultList: ObjectList = {
            admin: [
                '01',
                '1',
                '7',
                '11',
                '12',
                '13',
                '14',
                '15'
            ],
            user: ['02','100', '101','102','104', '105', '7'],
        };
        const username = localStorage.getItem('vuems_name');
        console.log(username);
        return {
            key: (username == 'admin' ? defaultList.admin : defaultList.user) as string[],
            defaultList,
        };
    },
    actions: {
        handleSet(val: string[]) {
            this.key = val;
        },
    },
});
