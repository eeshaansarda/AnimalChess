public class Piece{

    private Animal animal;
    private boolean side;       //True means home and False means away

    public Piece(Animal animal, boolean side){
        this.animal = animal;
        this.side = side;
    }

    public Piece duplicatePiece(){
        Piece pieceObj = new Piece(animal, side);
        return pieceObj;
    }

    public Animal getAnimal(){
        return animal;
    }

    public boolean getSide(){
        return side;
    }

    public void capture(){
        if(animal == Animal.ROOSTER){
            animal = Animal.CHICK;
        }
        side = !side;
    }

    public void promote(){
        if(animal == Animal.CHICK){
            animal = Animal.ROOSTER;
        }
    }

}
