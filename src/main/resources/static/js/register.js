var deferredPrompt = null;

window.addEventListener('beforeinstallprompt', function (e) {
  e.preventDefault();
  deferredPrompt = e;
});

window.installPWA = function () {
  if (!deferredPrompt) return;
  deferredPrompt.prompt();
};

window.addEventListener('appinstalled', function () {
  deferredPrompt = null;
});

if ('serviceWorker' in navigator) {
  navigator.serviceWorker.register('/sw.js');
}
