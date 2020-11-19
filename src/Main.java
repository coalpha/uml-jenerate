interface Main {
   static void main(final String[] args) {
      new gui();
      // if the user ran it with javaw
      // if (System.console() == null) {
      //    new gui();
      // } else {
      //    new cli(args);
      // }
   }
}
