
export interface User {
    canteenId:number;
    canteenName: string;
    restaurantName: string;
    restaurantId:number;
    password: string;
    dishCount:string;
}

export interface Register {
    username: string;
    password: string;
    email: string;
}