from threading import Condition, Thread
from tkinter import *
from tkinter.ttk import *
from multiprocessing import Process
import socket, time, os, sys

condition = Condition()
buffer = []

class ProducerThread(Thread):
    global buffer

    def run(self):
        def run_streaming_script():
            os.system('./streaming_server.sh')
    
        def close_streaming_script():
            os.system('./close_streaming.sh')
        
        self.root = Tk()
        self.root.geometry("300x280+100+100")
        self.root.title("Server Simulator")

        #state
        state_frame = Frame(self.root)
        state_frame.pack(fill=X)

        lblStreaming = Label(state_frame, text="Streaming state : ")
        lblStreaming.grid(row=0, column=0)
        lblStreamingInfo = Label(state_frame, text="Off", foreground="red", font=("Bold",16) )
        lblStreamingInfo.grid(row=0, column=1)
        
        #event state
        state_frame2 = Frame(self.root)
        state_frame2.pack(fill=X)
        
        lblLastEvent = Label(state_frame2, text="Last event : ")
        lblLastEvent.grid(row=0, column=0)
        lblLastEventInfo = Label(state_frame2, text="None")
        lblLastEventInfo.grid(row=0, column=1)
        
                
        def onStreaming():
            if lblStreamingInfo['text'] == 'On':
                lblLastEventInfo['text'] = 'Already streaming On'
            else:
                lblStreamingInfo['text'] = 'On'
                lblStreamingInfo['foreground'] = 'green'
                run_shell = Process(target=run_streaming_script, args=())
                run_shell.start()
        def offStreaming():
            if lblStreamingInfo['text'] == 'Off':
                lblLastEventInfo['text'] = 'Already streaming Off'
            else:
                lblStreamingInfo['text'] = 'Off'
                lblStreamingInfo['foreground'] = 'red'
                run_shell = Process(target=close_streaming_script, args=())
                run_shell.start()
                
        def EventA():
            condition.acquire()
            buffer.append('Event A')
            condition.notify()
            condition.release()
        def EventB():
            condition.acquire()
            buffer.append('Event B')
            condition.notify()
            condition.release()
        def EventC():
            condition.acquire()
            buffer.append('Event C')
            condition.notify()
            condition.release()
            
        #buttons
        button_frame = Frame(self.root)
        button_frame.pack(fill=X)
        
        btn_start = Button(button_frame, text="Streaming start", command=onStreaming)
        btn_start.pack(pady=10)
        
        btn_close = Button(button_frame, text="Streaming close", command=offStreaming)
        btn_close.pack(pady=10)
        
        btn_eventA = Button(button_frame, text="Event A", command=EventA)
        btn_eventA.pack(pady=10)
        
        btn_eventB = Button(button_frame, text="Event B", command=EventB)
        btn_eventB.pack(pady=10)
        
        btn_eventC = Button(button_frame, text="Event C", command=EventC)
        btn_eventC.pack(pady=10)

        self.root.mainloop()

class ConsumerThread(Thread):
    def __init__(self, client):
        Thread.__init__(self)
        self.client = client

    def run(self):
        global buffer
        while True:
            condition.acquire()
            if not buffer:
                print("Noting in buffer, cosumer waiting...")
                condition.wait()
                print("Consumer waked up!")
            event = buffer.pop(0) + '\n'
            self.client.send(event.encode('utf-8'))
            condition.release()
            time.sleep(1)

def main():
    server = socket.socket()
    host = '192.168.0.15'
    port = 5555
    server.bind((host, port))
    server.listen(1)

    print('[Waiting for connection..]')
    c, addr = server.accept()
    print('Got connection from', addr)

    consumer = ConsumerThread(c)
    consumer.daemon = True
    consumer.start()

    producer = ProducerThread()
    producer.start()
    producer.join()

    #do something such as logging on file, etc
    print('Program Finished')

if __name__ == '__main__':
    main()
