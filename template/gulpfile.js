var gulp = require('gulp'),
    watch = require('gulp-watch');
var batch = require('gulp-batch');
var mavenTask = require('gulp-maven-integration');

gulp.task('stream', function () {
	// Endless stream mode
    return watch('src/**/*.java', { ignoreInitial: false })
        .pipe(gulp.dest('install'));
});

gulp.task('watch', function () {
    watch('src/**/*.java', batch(function (events, done) {
        gulp.start('install', done);
    }));
});

mavenTask('start', 'install', "..");
mavenTask('install', 'install');

gulp.task('default', [ 'start', 'watch' ]);