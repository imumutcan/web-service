import { configureStore, createSlice } from "@reduxjs/toolkit";
import { loadAuthState, storeAuthState } from "./storage";



const authSlice = createSlice({
    name:'auth',
    initialState:loadAuthState(),
    reducers:{
        loginSuccess: (state, action) =>{
            state.id = action.payload.id;
            state.username = action.payload.username;
            state.email = action.payload.email;
            state.image = action.payload.image;
            state.token = action.payload.token;
            storeAuthState(state)
        },
        logoutSuccess: (state, action) =>{
            state.id = 0;
            delete state.username;
            delete state.email;
            delete state.image;
        },
        userUpdateSuccess:(state, action) =>{
            state.username = action.payload;
            storeAuthState(state);
        }
    }
})

export const { loginSuccess, logoutSuccess, userUpdateSuccess} = authSlice.actions; 




export const store = configureStore({
    reducer:{
        auth:authSlice.reducer
    }
})


