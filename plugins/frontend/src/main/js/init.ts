if ('serviceWorker' in navigator) {
    window.addEventListener('load', () => {
        const nav: any = navigator; // TS complained with Type 'WorkerNavigator' is not assignable to type 'Navigator'.
        nav.serviceWorker.register('/assets/js/serviceworker.js').then(registration => {
            // Registration was successful
            console.log('ServiceWorker registration successful with scope: ', registration.scope);
        }, function(err) {
            // registration failed :(
            console.log('ServiceWorker registration failed: ', err);
        });
    });
}