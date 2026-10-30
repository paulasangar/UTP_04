FINAL REPORT – THREAD CONTROL AND CONCURRENCY MODELS

1.  Introduction

The purpose of this assignment was to compare several Java concurrency
models by implementing moving balls in a graphical application. Each
ball moves independently, detects collisions with the panel boundaries,
and can be paused and resumed using the Shake Balls button.

2.  Description of the implementations

ThreadBall uses a traditional Java Thread for each ball. The balls share
a class-level MONITOR object. The synchronized block, wait(), and
notifyAll() methods provide a way to pause and resume ball threads. This
approach is straightforward, but creating many platform threads can
consume significant memory and operating-system resources.

ScheduledBall uses a ScheduledExecutorService to execute each ball’s
run() method periodically. A ScheduledFuture represents the scheduled
task and allows it to be cancelled and restarted. The scheduler in this
implementation has one worker thread, so scheduled tasks share that
worker. This reduces the number of platform threads, but a slow or
blocked task can delay other scheduled tasks.

VThreadBall uses Java virtual threads submitted to a shared
ExecutorService created by Executors.newVirtualThreadPerTaskExecutor().
A ReentrantLock and Condition coordinate pausing and resuming. The
paused flag records the state, while await() and signalAll() suspend and
wake the virtual thread. Virtual threads are lightweight and are
suitable for applications with many concurrent tasks that spend time
waiting.

VsThreadBall also uses virtual threads, but controls pausing with a
Semaphore. A ball acquires the semaphore during its execution. On a
collision, it waits for another permit; wakeUp() releases a permit so
the ball can continue. A semaphore is useful for permit-based
coordination, although its permit count must be managed carefully to
avoid unintended extra releases or blocking.

3.  Observations and comparison

Traditional threads provide independent execution and are easy to
understand. However, every ball requires a platform thread, which can
become expensive as the number of balls increases.

The scheduled executor centralizes periodic execution and can reduce
thread-management overhead. With a single-thread scheduler, however, all
scheduled balls share one worker. This can limit parallelism and make
the animation less responsive if a task takes too long.

Virtual threads are lightweight compared with platform threads. They
make it practical to represent many independently waiting tasks without
creating the same number of operating-system threads. In this animation,
the work per ball is small, so the difference may not be obvious with
only a few balls. The benefits become more noticeable with a larger
number of concurrent tasks, especially tasks that spend substantial time
waiting.

The Lock/Condition implementation makes the paused state explicit and
allows a thread to wait until it is signalled. The Semaphore
implementation uses permits to control whether a ball can proceed. Both
approaches require correct synchronization and careful handling of
interruption and wake-up signals.

4.  Responsiveness and resource usage

For a small number of balls, all implementations can provide acceptable
responsiveness. Traditional threads are simple but have higher
per-thread resource costs. A single-thread scheduled executor uses fewer
platform threads but can serialize work. Virtual threads generally offer
lower resource overhead for large numbers of waiting tasks.

The animation also uses a separate repainting thread. The ball
coordinates are accessed by both worker threads and the AWT
event-dispatch/painting thread. A production-quality implementation
should make shared state visibility and painting consistent, for example
by using synchronization or suitable volatile/atomic state, and should
stop executors and worker tasks cleanly when the window closes.

5.  Advantages and limitations

ThreadBall: Advantage: Simple direct thread control and
easy-to-understand synchronization. Limitation: Platform-thread resource
use grows with the number of balls; wait/notify coordination must avoid
lost notifications.

ScheduledBall: Advantage: Periodic execution is managed by an executor,
and ScheduledFuture supports cancellation. Limitation: A single-thread
scheduler serializes tasks; cancellation and restart logic needs careful
handling.

VThreadBall: Advantage: Lightweight virtual threads and explicit
Lock/Condition coordination. Limitation: Requires Java 21 or later;
locking and interruption must be handled correctly.

VsThreadBall: Advantage: Virtual threads combined with a simple
permit-based synchronization mechanism. Limitation: Semaphore permits
can accumulate if released unnecessarily, and incorrect acquire/release
ordering can cause blocking or unexpected behaviour.

6.  Conclusion

For an application that may create many independently waiting balls,
virtual threads are likely to be the most efficient and scalable
approach because they reduce the resource cost associated with large
numbers of platform threads. VThreadBall provides clear pause/resume
semantics through Lock and Condition, while VsThreadBall demonstrates
semaphore-based coordination. The scheduled executor is useful when
tasks need periodic execution, but a single-thread scheduler can become
a bottleneck. The best choice depends on the workload: virtual threads
are attractive for many concurrent waiting tasks, scheduled executors
for periodic jobs, and traditional threads for simpler applications with
a small number of workers.

7.  Testing notes

The application should be tested by creating several balls of each type,
observing their movement and boundary collisions, and pressing Shake
Balls after collisions. Tests should also include creating many balls
and closing the window. Actual performance conclusions should be based
on measurements on the computer used for the demonstration; no benchmark
measurements are claimed in this report.