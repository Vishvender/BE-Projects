#include<iostream>
#include<math.h>
#include<stdlib.h>
#include<string.h>
using namespace std;
struct node{ //to save a special character on every point on curve
  int x,y;
  char specialChar;
  node *next;
};
struct point{
  int x,y;
};
void showpoint(point a){
  cout<<a.x<< " "<<a.y<<endl;
}
int mod(long u,long v=37){ // calculate modulus
  int t=u%v;
  if(t<0) t=t+v;
  return t;
}
long modinv(long a,long b=37){
  for(long i=0;i<b;i++){
    if((a*i)%b==1){
      return i;
    }
  }
}
class ECC{
public:
  int a,b,p; //variables of our elliptic curve
  string initialSpecialString;
  int counterOfSpCharArray;
  struct node *root,*curr;
  ECC(){
    root=new node();
    node *n;
    n=new node();
    n->x=37;
    n->y=37;
    n->specialChar='*';
    n->next=NULL;
    root->next=n;
    curr=n;
    a=2;// define the variables of curve
    b=9;
    p=37;
    initialSpecialString="abcdefghijklmnopqrstuvwxyz0123456789~-@#+^&";
    counterOfSpCharArray=0; // starting from char='a'
    // find points on curve through checking every value of x from 0 to p
    for(int xi=0;xi<37;xi++){
      int q=mod(pow(xi,3)+a*xi+b);
      for(int yj=0;yj<37;yj++){
        int p=mod(pow(yj,2));
        if(p==q){
          // node *n;
          n=new node();
          n->x=xi;
          n->y=yj;
          n->specialChar=initialSpecialString[counterOfSpCharArray++];
          n->next=NULL;
          if(root->next==NULL){
            root->next=n;
            curr=n;
          }
          else{
            curr->next=n;
            curr=n;
          }
        }
      }
    }
  }
  node* findRandomPointOnCurve(int count){
    node *temp=root;
    int i=0;
    while(temp->next!=NULL){
      temp=temp->next;
      i++;
      if(i==count){
        break;
      }
    }
    return temp;
  }
  char findCharaterForPoint(struct point pointToCheck){
    node *temp=root;
    while(temp->next!=NULL){
      temp=temp->next;
      if(temp->x==pointToCheck.x){
        if(temp->y==pointToCheck.y){
          break;
        }
      }
    }
    return temp->specialChar;
  }
  struct point findPointForCharacter(char characterToCheck){
    node *temp=root;
    while(temp->next!=NULL){
      temp=temp->next;
      if(temp->specialChar==characterToCheck) break;
    }
    if(characterToCheck=='*'){
      temp=root->next;
    }
    struct point pointToReturn;
    pointToReturn.x=temp->x;
    pointToReturn.y=temp->y;
    return pointToReturn;
  }
  void display(){
    node *temp=root;
    while(temp->next!=NULL){
      temp=temp->next;
      cout<<"("<<temp->x<<","<<temp->y<<")->"<<temp->specialChar<<endl;
    }
  }
}e;
struct point addPoint(struct point A,struct point B){
  struct point temp;
  long s;
  if(A.x==37 || B.x==37){
    if(A.x==37){
      temp.x==B.x; temp.y=mod(B.y,37);
    }
    else{
      temp.x==A.x; temp.y=mod(A.y,37);
    }
  }
  else{
    if(A.x==B.x){
      if(A.y==B.y){
        s=mod(3*A.x*A.x+2)*modinv(mod(2*A.y));
        s=mod(s);
        temp.x=mod(s*s-2*A.x);
        temp.y=mod(s*(mod(A.x-temp.x))-A.y);
      }
      else{
        temp.x=37;
        temp.y=37;
      }
    }
    else{
      s=mod(A.y-B.y)*modinv(mod(A.x-B.x));
      s=mod(s);
      temp.x=mod(s*s-A.x-B.x);
      temp.y=mod(s*(mod(A.x-temp.x))-A.y);
    }
  }
  return temp;
}
struct point multPoint(int multiplier,struct point A){
  struct point temp;
  temp=A;
  multiplier=multiplier%43;
  if(multiplier==0){
    multiplier=1;
  }
  // cout<<"yes "<<multiplier<<endl;
  multiplier--;
  while(multiplier--){
    temp=addPoint(A,temp);
  }
  return temp;
}
class person{
private:
  int alpha;
  struct point A,Ab;
public:
  struct point A1,A2;
  person(){
    node *n;
    alpha=rand()%37+1;
    int a=rand()%42+1;
    n=e.findRandomPointOnCurve(a);
    A.x=n->x;
    A.y=n->y;
    A2=multPoint(alpha,A);
  }
  int getAlpha(){
    return alpha;
  }
  struct point getAb(){
    return Ab;
  }
  void calculateA1(struct point C){
    struct point temp;
    temp=addPoint(A,C);
    A1=multPoint(alpha,temp);
  }
  void calculateAb(struct point B2){
    Ab=multPoint(alpha,B2);
  }
  void showRan(){
    cout<<"alpha is "<<alpha<<endl;
    cout<<"A is "<<"("<<A.x<<","<<A.y<<")"<<endl;
    cout<<"Ab is "<<"("<<Ab.x<<","<<Ab.y<<")"<<endl;
    cout<<"A1 is "<<"("<<A1.x<<","<<A1.y<<")"<<endl;
    cout<<"A2 is "<<"("<<A2.x<<","<<A2.y<<")"<<endl;
  }
};
class newCouple{
public:
  struct point C;
  person *first,*two;
  newCouple(person *A,person *B){
    first=A;
    two=B;
    node *n;
    n=e.findRandomPointOnCurve(rand()%42+1);
    C.x=n->x;
    C.y=n->y;
    A->calculateA1(C);
    B->calculateA1(C);
    A->calculateAb(B->A2);
    B->calculateAb(A->A2);
  }
  string newMessage(person *sender,string newMsg){
    string encrypted;
    for(int i=0;i<newMsg.length();i++){
      int delta=rand()%37+1;
      struct point E1,E2,T1,T2,T3,T4,M;
      E1=multPoint(delta,C);
      M=e.findPointForCharacter(newMsg[i]);
      T1=M;
      T2=multPoint(delta+two->getAlpha(),first->A1);
      T3=multPoint(delta,first->A2);
      T3.y=(-1*T3.y);
      T4=first->getAb();
      E2=addPoint(addPoint(T1,T2),addPoint(T3,T4));
      char n1,n2;
      n1=e.findCharaterForPoint(E1);
      n2=e.findCharaterForPoint(E2);
      encrypted=encrypted+n1;
      encrypted=encrypted+n2;
    }
    return encrypted;
  }
  string decryptMessage(person *receiver,string encrypted){
    int i=0,t=0;
    string decrypted;
    while(i<encrypted.length()){
      t++;
      char n1=encrypted[i++];
      char n2=encrypted[i++];
      point E1,E2,M,T1,T2,T3;
      E1=e.findPointForCharacter(n1);
      E2=e.findPointForCharacter(n2);
      T1=multPoint(first->getAlpha(),E1);
      T2=multPoint(first->getAlpha(),two->A1);
      T3=two->getAb();
      M=addPoint(T2,T3);
      M=addPoint(T1,M);
      M.y=(-1*M.y);
      M=addPoint(E2,M);
      decrypted=decrypted+e.findCharaterForPoint(M);
    }
    return decrypted;
  }
  void showdata(){
    cout<<"C is "<<"("<<C.x<<","<<C.y<<")"<<endl;
  }
};
int main(int argc, char **argv){
  // e.display();
  person alice;
  person bob;
  newCouple alicebob(&alice,&bob);

  string msg;
  msg=argv[2];
if((int)argv[1][0]-48==1){
    string retMsg=alicebob.newMessage(&alice,msg);
    cout<<retMsg<<endl;
  }
 else{
    string origMsg;
    origMsg=alicebob.decryptMessage(&bob,msg);
    cout<<origMsg<<endl;
  }
  return 0;
}
