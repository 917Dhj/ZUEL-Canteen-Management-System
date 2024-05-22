export interface Menus {
    canteenName:string;
    restaurantName:string;
    dishName:string;
    dishId:number;
    dishStatus:string;
}

export interface Menu {
    id: string;
    pid?: string;
    icon?: string;
    index: string;
    title: string;
    permiss?: string;
    children?: Menu[];
}