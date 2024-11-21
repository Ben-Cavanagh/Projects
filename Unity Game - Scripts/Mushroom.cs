// Include packages
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.InputSystem;

[RequireComponent(typeof(SpriteRenderer))] 
public class Mushroom : MonoBehaviour
{
    // Define input action variables
    private PlayerActions actions;
    private InputAction growMushroomAction;

    // Define variable for the mushroom state
    private State state;

    // Define collision variables (based on layer number)
    private bool colliding = false;
    private int collidingObject;
    [SerializeField] private int player = 6;
    [SerializeField] private int enemy = 10;
    [SerializeField] private int spellbolt = 7;

    // Define scale change variable
    [Range(0,1)] [SerializeField] private float growRate = 0.5f;
    [Range(0,1)] [SerializeField] private float shrinkRate = 0.5f;
    [Range(0,1)] [SerializeField] private float decreaseValue = 1.5f;
    [Range(1,4)] [SerializeField] private float maxSize = 3.5f;
    [Range(1,4)] [SerializeField] private float minSize = 1.0f;
    private Vector3 growVector;
    private Vector3 shrinkVector;
    private Vector3 decreaseVector;
    private Vector3 deadVector;

    // Define colour variables
    SpriteRenderer sprite;
    private Color transparent = new Color(0,0,0,0);
    [SerializeField] private Color growColour = Color.white;
    [SerializeField] private Color fullSizeColour = Color.green;
    [SerializeField] private Color shrinkColour = Color.red;

    // Define variables for poison timer
    [Range(1,4)] [SerializeField] private float poisonTimer;
    private float resetTimer = 3f;

    // Define level manager
    [SerializeField] private LevelManager levelManager;

    // Define all possible states
    private enum State
    {
        Dead,
        Growing,
        FullyGrown,
        Poisoned
    }

    private void Awake()
    {
        actions = new PlayerActions();
        growMushroomAction = actions.Actions.growMushroom;
    }

    private void OnEnable()
    {
        growMushroomAction.Enable();
    }

    private void OnDisable()
    {
        growMushroomAction.Disable();
    }

    private void OnTriggerStay2D(Collider2D collider)
    {
        colliding = true;
        collidingObject = collider.gameObject.layer;
    }

    private void OnTriggerEnter2D(Collider2D collider)
    {
        colliding = true;
        collidingObject = collider.gameObject.layer;
    }

    private void OnTriggerExit2D(Collider2D collider)
    {
        colliding = false;
    }

    private void Start()
    {
        // Initise mushroom state to dead
        state = State.Dead;

        // Get sprite renderer
        sprite = GetComponent<SpriteRenderer>();

        // Set reset timer value (in seconds)
        resetTimer = poisonTimer;

        // Initialise vectors
        growVector = new Vector3(growRate, growRate, 0f);
        shrinkVector = new Vector3(shrinkRate, shrinkRate, 0f);
        decreaseVector = new Vector3(decreaseValue, decreaseValue, 0f);
        deadVector = new Vector3(minSize, minSize, 0f);
    }

    private void Update()
    {
        // When level resets, reset mushrooms
        if (levelManager.ResetLevel)
        {
            state = State.Dead;
        }
        else
        {
            RunMushroomStates();
        }
    }

    private void RunMushroomStates()
    {
        switch (state)
        {
            case State.Dead:
                RunDeadState();
                break;
            case State.Growing:
                RunGrowingState();
                break;
            case State.FullyGrown:
                RunFullyGrownState();
                break;
            case State.Poisoned:
                RunPoisonedState();
                break;
        }  
    }

    // Case where the current state is Dead
    private void RunDeadState()
    {
        sprite.color = transparent;
        transform.localScale = deadVector;

        // Case where player grows a mushroom
        if ((growMushroomAction.ReadValue<float>() == 1) && (colliding) && (collidingObject == player))
        {
            state = State.Growing;
        }
    }

    // Case where the current state is Growing
    private void RunGrowingState()
    {
        sprite.color = growColour;
        transform.localScale += (growVector * Time.deltaTime);

        // Case where the mushroom grows to full size
        if (transform.localScale.x >= maxSize)
        {
            state = State.FullyGrown;
        }

        state = CheckEnemy(state);
        state = CheckSpellbolt(state);
        state = CheckMinSize(state);
    }

    // Case where the current state is Fully Grown
    private void RunFullyGrownState()
    {
        sprite.color = fullSizeColour;

        // Case where player collects the mushroom
        if ((colliding) && (collidingObject == player))
        {
            levelManager.CurrentScore += 10;
            state = State.Dead;
        }
        state = CheckEnemy(state);
        state = CheckSpellbolt(state);
    }

    // Case where the current state is Poisoned
    private void RunPoisonedState()
    {
        sprite.color = shrinkColour;
        transform.localScale -= (shrinkVector * Time.deltaTime);
        poisonTimer -= Time.deltaTime;

        // Case where poison timer runs out
        if (poisonTimer <= 0f)
        {
            poisonTimer = resetTimer;
            state = State.Growing;
        }
        state = CheckEnemy(state);
        state = CheckSpellbolt(state);
        state = CheckMinSize(state);
    }

    // Case where a ghoul gets to the mushroom
    private State CheckEnemy(State state)
    {
        if ((colliding) && (collidingObject == enemy))
        {
            if ((state == State.Growing) || (state == State.FullyGrown))
            {
                state = State.Poisoned;
            }
            else if (state == State.Poisoned)
            {
                poisonTimer = resetTimer;
            }
            colliding = false;
        }
        return state;
    }

    // Case where the player's own spellbolt hits the mushroom
    private State CheckSpellbolt(State state)
    {
        if ((colliding) && (collidingObject == spellbolt))
        {
            if ((state == State.Growing) || (state == State.FullyGrown) || (state == State.Poisoned))
            {
                transform.localScale -= decreaseVector;
                colliding = false;
            }
            if ((state == State.Growing) || (state == State.FullyGrown))
            {
                state = State.Growing;
            }
        }
        return state;
    }

    // Case where the mushroom shrinks to minimum size
    private State CheckMinSize(State state)
    {
        if (transform.localScale.x < minSize)
        {
            poisonTimer = resetTimer;
            state = State.Dead;
        }
        return state; 
    }
}
