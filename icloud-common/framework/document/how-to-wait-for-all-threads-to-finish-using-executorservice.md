http://stackoverflow.com/questions/1250643/how-to-wait-for-all-threads-to-finish-using-executorservice

#1
ExecutorService taskExecutor = Executors.newFixedThreadPool(4);
int numberOfTasks=0;
Semaphore s=new Semaphore(0);
while(...) {
    taskExecutor.execute(new MyTask());
    numberOfTasks++;
}

try {
    s.aquire(numberOfTasks);
...
In your task just call s.release() as you would latch.countDown();





#2
CountDownLatch latch = new CountDownLatch(totalNumberOfTasks);
ExecutorService taskExecutor = Executors.newFixedThreadPool(4);
while(...) {
  taskExecutor.execute(new MyTask());
}

try {
  latch.await();
} catch (InterruptedException E) {
   // handle
}
and within your task (enclose in try / finally)

latch.countDown();


#3
CountDownLatch latch = new CountDownLatch(totalNumberOfTasks);
ExecutorService taskExecutor = Executors.newFixedThreadPool(4);
while(...) {
  taskExecutor.execute(new MyTask());
}

try {
  latch.await();
} catch (InterruptedException E) {
   // handle
}
and within your task (enclose in try / finally)

latch.countDown();


#4
The CyclicBarrier class in Java 5 and later is designed for this sort of thing.







