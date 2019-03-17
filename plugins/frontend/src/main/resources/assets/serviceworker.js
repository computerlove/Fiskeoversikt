const CACHE_NAME = 'landingsopplysninger-${git.commit.id.abbrev}';
const urlsToCache = [
    '/',
    '/assets/styles.css',
    '/assets/vendor.bundle.js',
    '/assets/index.html',
    '/assets/app.bundle.js',
    '/assets/images/ic_refresh_white_24px.svg'
];

self.addEventListener('install', function(event) {
    console.log('Installing');

    event.waitUntil(
        caches.open(CACHE_NAME)
            .then(function(cache) {
                console.log('Opened cache');
                return cache.addAll(urlsToCache);
            })
    );
});

self.addEventListener('activate', function(event) {

    const cacheWhitelist = [CACHE_NAME];

    event.waitUntil(
        caches.keys().then(function(cacheNames) {
            return Promise.all(
                cacheNames.map(function(cacheName) {
                    if (cacheWhitelist.indexOf(cacheName) === -1) {
                        console.log("Removing " + cacheName);
                        return caches.delete(cacheName);
                    }
                })
            );
        })
    );
});